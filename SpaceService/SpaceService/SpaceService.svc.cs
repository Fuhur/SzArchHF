using SpaceService.Model;
using SpaceService.Model.DTO;
using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace SpaceService
{
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single)]
    public class SpaceService : ISpaceService
    {
        private SpaceEntities db = new SpaceEntities();

        private List<string> lobby = new List<string>();
        private List<Match> matches = new List<Match>();

        private static readonly DateTime origin = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);

        public void SetName(string deviceId, string name)
        {
            if (deviceId == null || name == null)
            {
                return;
            }

            var player = GetOrCreatePlayer(deviceId);
            player.Name = name;

            db.SaveChanges();
        }

        public void UploadHighScore(string deviceId, int score)
        {
            if (deviceId == null || score == 0)
            {
                return;
            }

            var player = GetOrCreatePlayer(deviceId);
            if (player.HighScore == null || score < player.HighScore)
            {
                player.HighScore = score;
            }

            db.SaveChanges();
        }

        public List<HighScoreDTO> HighScores()
        {
            return db.Players.Where(p => p.HighScore.HasValue).OrderBy(p => p.HighScore).Take(10).Select(p => new HighScoreDTO
            {
                Name = p.Name,
                Score = p.HighScore.Value
            }).ToList();
        }

        public StartMultiplayerResponse StartMultiplayer(string deviceId)
        {
            var response = new StartMultiplayerResponse();

            // Check device id
            var player = GetOrCreatePlayer(deviceId);

            Match match = null;

            // Already in game
            if (matches.Any(m => m.PlayerStates.Any(ps => ps.Player.DeviceId == deviceId)))
            {
                match = matches.FirstOrDefault(m => m.PlayerStates.Any(ps => ps.Player.DeviceId == deviceId));
                if (match.StartTimeStamp < TimeInMillis())
                {
                    matches.Remove(match);
                    match = null;
                }
            }

            if (match == null)
            {
                // Join the lobby
                if (!lobby.Contains(deviceId))
                {
                    lobby.Add(deviceId);
                }

                // Create match
                if (lobby.Count > 1)
                {
                    lobby.Remove(deviceId);

                    var opponentDeviceId = lobby.First();
                    var opponent = GetOrCreatePlayer(opponentDeviceId);
                    lobby.Remove(opponentDeviceId);

                    match = new Match()
                    {
                        LevelSeed = new Random().Next(1000),
                        StartTimeStamp = TimeInMillis() + 5000
                    };
                    match.PlayerStates[0] = new PlayerState { Player = player };
                    match.PlayerStates[1] = new PlayerState { Player = opponent };
                    matches.Add(match);
                }
            }

            if (match != null)
            {
                response.Ready = true;
                response.LevelSeed = match.LevelSeed;
                response.LevelLength = match.LevelLength;
                response.StartTimeStamp = match.StartTimeStamp;
                return response;
            }
            else
            {
                response.Ready = false;
                return response;
            }
        }

        public void QuitLobby(string deviceId)
        {
            lobby.Remove(deviceId);
        }

        private long TimeInMillis()
        {
            return (long)(DateTime.UtcNow - origin).TotalMilliseconds;
        }

        private Player GetOrCreatePlayer(string deviceId)
        {
            var player = db.Players.FirstOrDefault(p => p.DeviceId == deviceId);
            if (player == null)
            {
                player = new Player()
                {
                    DeviceId = deviceId
                };
                db.Players.Add(player);
                db.SaveChanges();
            }
            return player;
        }

        public TickResponse Tick(string deviceId, Vector position, Vector velocity)
        {
            var response = new TickResponse();

            var match = matches.FirstOrDefault(m => m.PlayerStates.Any(ps => ps.Player.DeviceId == deviceId));
            if (match == null)
            {
                return null;
            }
            var playerState = match.PlayerStates.FirstOrDefault(ps => ps.Player.DeviceId == deviceId);
            playerState.Position = position;
            playerState.Velocity = velocity;

            var opponentPlayerState = match.PlayerStates.FirstOrDefault(ps => ps.Player.DeviceId != deviceId);
            response.OpponentPosition = opponentPlayerState.Position;
            response.OpponentVelocity = opponentPlayerState.Velocity;
            return response;
        }

        public void Finish(string deviceId, int score)
        {
            var match = matches.FirstOrDefault(m => m.PlayerStates.Any(ps => ps.Player.DeviceId == deviceId));
            if (match == null)
            {
                return;
            }

            var playerState = match.PlayerStates.FirstOrDefault(ps => ps.Player.DeviceId == deviceId);
            playerState.Finished = true;
            playerState.Score = score;
            UploadHighScore(deviceId, score);
        }

        public string Result(string deviceId)
        {
            string result = null;

            var match = matches.FirstOrDefault(m => m.PlayerStates.Any(ps => ps.Player.DeviceId == deviceId));
            if (match == null)
            {
                return null;
            }

            var playerState = match.PlayerStates.FirstOrDefault(ps => ps.Player.DeviceId == deviceId);
            var opponentPlayerState = match.PlayerStates.FirstOrDefault(ps => ps.Player.DeviceId != deviceId);

            if (opponentPlayerState.Finished)
            {
                if (Math.Abs(playerState.Score - opponentPlayerState.Score) < 100)
                {
                    result = "Draw";
                }
                else
                {
                    result = playerState.Score < opponentPlayerState.Score ? "Victory" : "Defeat";
                }
            }
            else
            {
                result = "Victory";
            }
            playerState.ResultRequested = true;

            //if (!match.PlayerStates.Any(ps => !ps.ResultRequested))
            //{
            //    matches.Remove(match);
            //}

            return result;
        }

        public long Delay(long timestamp)
        {
            return TimeInMillis() - timestamp;
        }

        public long ServerTime()
        {
            return TimeInMillis();
        }

        ////////////////////////////////////////

        public List<string> Lobby()
        {
            return lobby.ToList();
        }

        public List<Match> Matches()
        {
            return matches.ToList();
        }

        public void Reset()
        {
            lobby.Clear();
            matches.Clear();
        }
    }
}
