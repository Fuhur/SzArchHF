using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Web;

namespace SpaceService.Model
{
    public class Match
    {
        public int LevelLength { get; set; }
        public int LevelSeed { get; set; }
        public long StartTimeStamp { get; set; }

        public PlayerState[] PlayerStates { get; set; }

        public Match()
        {
            PlayerStates = new PlayerState[2];
            LevelLength = 30;
        }
    }
}