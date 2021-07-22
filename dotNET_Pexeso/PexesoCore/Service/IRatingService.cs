using System.Collections.Generic;
using PexesoGame.Entity;

namespace PexesoGame.Service
{
    public interface IRatingService
    {
        void AddRating(Rating rating);

        IList<Rating> GetRatings();

        decimal GetAverageRating();

        void ClearRatings();
    }
}
