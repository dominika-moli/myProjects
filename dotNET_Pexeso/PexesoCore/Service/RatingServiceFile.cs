using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization.Formatters.Binary;
using PexesoGame.Entity;
using System;

namespace PexesoGame.Service
{
    public class RatingServiceFile : IRatingService
    {
        private const string FileName = "rating.bin";

        private List<Rating> ratings = new List<Rating>();

        public void AddRating (Rating rating)
        {
            GetRatings();
            ratings.Add(rating);
            SaveRatings();
        }

        public IList<Rating> GetRatings()
        {
            LoadRatings();
            return (from r in ratings select r).ToList();

        }
 
        public decimal GetAverageRating()
        {
            LoadRatings();

            if (ratings.Count == 0)
            {
                return 0;
            }
            else 
            {
                Decimal sum = 0;
                Decimal count = ratings.Count;
                for (var i = 0; i < count; i++ )
                {
                    sum = sum + ratings.ElementAt(i).NumberOfStars;
             }
             return Decimal.Divide(sum, count);
            }
        }

        public void ClearRatings()
        {
            ratings.Clear();
            File.Delete(FileName);
        }

        private void SaveRatings()
        {
            using (var fs = File.OpenWrite(FileName))
            {
                var bf = new BinaryFormatter();
                bf.Serialize(fs, ratings);
            }
        }

        private void LoadRatings()
        {
            if (File.Exists(FileName))
            {
                using (var fs = File.OpenRead(FileName))
                {
                    var bf = new BinaryFormatter();
                    ratings = (List<Rating>)bf.Deserialize(fs);
                }
            }
        }        
    }
}
