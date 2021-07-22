using System.Collections.Generic;
using PexesoGame.Entity;

namespace PexesoGame.Service
{
    public interface IScoreService
    {
        void AddScore(Score score);

        IList<Score> GetTopScores();

        void ClearScores();
    }
}
