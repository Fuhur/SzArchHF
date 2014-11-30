using SpaceOpponent.SpaceServiceReference;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Timers;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace SpaceOpponent
{
	/// <summary>
	/// Interaction logic for MainWindow.xaml
	/// </summary>
	public partial class MainWindow : Window
	{
		private SpaceServiceClient client = new SpaceServiceClient();

		private static readonly DateTime origin = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);

		private long startTimeStamp;
		private long delay;
		private int length;

		private Timer timer;

		private long lastTime = -1;
		private float Y;

		public MainWindow()
		{
			InitializeComponent();
		}

		private async void StartButton_Click(object sender, RoutedEventArgs e)
		{
			var deviceId = deviceTextBox.Text;
			client.SetName(deviceId, "BOT");
			var response = await client.StartMultiplayerAsync(deviceId);
			if (response.Ready)
			{
				WriteLine("Seed: " + response.LevelSeed);
				startTimeStamp = response.StartTimeStamp;

				var delay = client.Delay(TimeInMillis());
				startTimeStamp -= delay;
				length = response.LevelLength;

				timer = new Timer(50);
				timer.Elapsed += timer_StartGame;
				timer.Start();
			}
			else
			{
				WriteLine("Not ready.");
			}
		}

		private void timer_StartGame(object sender, ElapsedEventArgs e)
		{
			Dispatcher.Invoke((Action)(() => { WriteLine("Start in " + (startTimeStamp - TimeInMillis())); }));
			if (startTimeStamp < TimeInMillis())
			{
				timer.Stop();

				timer = new Timer(40);
				timer.Elapsed += timer_PlayGame;
				timer.Start();
			}
		}

		private void timer_PlayGame(object sender, ElapsedEventArgs e)
		{
			long actTime = TimeInMillis();

			long elapsed = 0;
			if (lastTime > 0)
			{
				elapsed = actTime - lastTime;
			}
			lastTime = actTime;

			Y += elapsed / 1000.0f * 0.6f;
			if (Y >= length)
			{
				lastTime = -1;
				Y = 0;

				Dispatcher.Invoke((Action)(() => 
				{
					client.Finish(deviceTextBox.Text, (int)(actTime - startTimeStamp));
				}));
				timer.Stop();
			}

			Dispatcher.Invoke((Action)(() =>
			{
				client.Tick(deviceTextBox.Text, 
					new SpaceOpponent.SpaceServiceReference.Vector { X = 0.5f, Y = Y }, 
					new SpaceOpponent.SpaceServiceReference.Vector { X = 0, Y = 0.6f });
				WriteLine("Y " + Y);
			}));
		}

		private long TimeInMillis()
		{
			return (long)(DateTime.UtcNow - origin).TotalMilliseconds;
		}

		private void ScrollButton_Click(object sender, RoutedEventArgs e)
		{
			if ((string)disableScrollButton.Content == "Disable scroll")
			{
				disableScrollButton.Content = "Enable scroll";
			}
			else
			{
				disableScrollButton.Content = "Disable scroll";
			}
		}

		private void ClearButton_Click(object sender, RoutedEventArgs e)
		{
			console.Text = "";
		}

		private void WriteLine(string text)
		{
			console.Text += text + Environment.NewLine;
			if ((string)disableScrollButton.Content == "Disable scroll")
			{
				scroller.ScrollToBottom();
			}
		}
	}
}
