using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using PexesoGame.Core;
using PexesoGame.Entity;

namespace PexesoGame.Models
{
    public class HomeModel
    {
        public string Player { get; set; }

        public int Size { get; set; }
        
        public IList<Score> Scores { get; set; }

        public IList<Comment> Comments { get; set; }

        public IList<Rating> Ratings { get; set; }

        public decimal AvgRating { get; set; }
    }
}