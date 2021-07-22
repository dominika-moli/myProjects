using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using PexesoGame.Core;
using PexesoGame.Entity;

namespace PexesoGame.Models
{
    public class PexesoModel
    {
        public Board Board { get; set; }

        public string Message { get; set; }

        public IList<Score> Scores { get; set; }

        public IList<Comment> Comments { get; set; }

        public IList<Rating> Ratings { get; set; }

        public decimal AvgRating { get; set; }
    }
}
