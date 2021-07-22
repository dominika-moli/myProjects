using System.Collections.Generic;
using System.Linq;
using Microsoft.EntityFrameworkCore;
using PexesoGame.Entity;
using System;

namespace PexesoGame.Service
{
    public class RatingServiceEF : IRatingService
    {
        public void AddRating(Rating rating)
        {
            using (var context = new PexesoDbContext())
            {
                context.Ratings.Add(rating);
                context.SaveChanges();
            }
        }

        public IList<Rating> GetRatings()
        {
            using (var context = new PexesoDbContext())
            {
                return (from c in context.Ratings select c).ToList();
            }
        }

        public decimal GetAverageRating()
        {
            using (var context = new PexesoDbContext())
            {
               var averageRating = context.Ratings.Average(c => c.NumberOfStars);     
               var decimalValue = Convert.ToDecimal(averageRating);          
               return (Math.Round(decimalValue,1));                          
            }  
                   
        }

        public void ClearRatings()
        {
            using (var context = new PexesoDbContext())
            {
                context.Database.ExecuteSqlRaw("DELETE FROM Ratings");
            }
        }
    }
}
