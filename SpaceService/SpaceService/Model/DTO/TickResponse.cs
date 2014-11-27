using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace SpaceService.Model.DTO
{
    public class TickResponse
    {
        public Vector OpponentPosition { get; set; }
        public Vector OpponentVelocity { get; set; }
    }
}