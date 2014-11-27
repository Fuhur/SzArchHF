using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace SpaceService.Model.DTO
{
    public class StartMultiplayerResponse
    {
        public bool Ready { get; set; }

        public int LevelSeed { get; set; }
        public int LevelLength { get; set; }
        public long StartTimeStamp { get; set; }
    }
}