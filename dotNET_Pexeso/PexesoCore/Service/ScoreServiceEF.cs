using System.Collections.Generic;
using System.Linq;
using Microsoft.EntityFrameworkCore;
using PexesoGame.Entity;

namespace PexesoGame.Service
{
    public class ScoreServiceEF : IScoreService
    {
        public void AddScore(Score score)
        {
            using (var context = new PexesoDbContext())
            {
                context.Scores.Add(score);
                context.SaveChanges();
            }
        }

        public IList<Score> GetTopScores()
        {
            using (var context = new PexesoDbContext())
            {
                return (from s in context.Scores
                    orderby s.Points
                        descending
                    select s).Take(10).ToList();
            }
        }

        public void ClearScores()
        {
            using (var context = new PexesoDbContext())
            {
                context.Database.ExecuteSqlRaw("DELETE FROM Scores");
            }
        }
    }
}
