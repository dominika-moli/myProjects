using PexesoGame.Entity;
using PexesoGame.Service;
using Xunit;


namespace PexesoGameTests
{
    public class ScoreTest
    {
        private IScoreService ScoreService()
        {
            var scoreService = new ScoreServiceFile();
            scoreService.ClearScores();
            return scoreService;
        }

        [Fact] 
         public void TestAddScore()
        {
            var scoreService = ScoreService();

            scoreService.AddScore(new Score { Player = "Janko", Points = 120 });
            var scores = scoreService.GetTopScores();

            var expected1 = 1;
            var actual1 = scores.Count;

            var expected2 = "Janko";
            var actual2 = scores[0].Player;

            var expected3 = 120;
            var actual3 = scores[0].Points;

        
            Assert.Equal<int>(expected1, actual1);
            Assert.Equal(expected2, actual2);
            Assert.Equal<int>(expected3, actual3);
        }

        [Fact] 
         public void TestGetTopScores()
        {
            var scoreService = ScoreService();

            scoreService.AddScore(new Score { Player = "Janko", Points = 120 });
            scoreService.AddScore(new Score { Player = "Matko", Points = 44 });
            scoreService.AddScore(new Score { Player = "Kubko", Points = 52 });
            scoreService.AddScore(new Score { Player = "Petko", Points = 12 });
            var scores = scoreService.GetTopScores();

            var expected1 = 3;
            var actual1 = scores.Count;

            var expected2 = "Janko";
            var actual2 = scores[0].Player;
            var expected3 = 120;
            var actual3 = scores[0].Points;


            var expected4 = "Kubko";
            var actual4 = scores[1].Player;
            var expected5 = 52;
            var actual5 = scores[1].Points;


            var expected6 = "Matko";
            var actual6 = scores[2].Player;           
            var expected7 = 44;
            var actual7 = scores[2].Points;

        
            Assert.Equal<int>(expected1, actual1);

            Assert.Equal(expected2, actual2);
            Assert.Equal<int>(expected3, actual3);
            Assert.Equal(expected4, actual4);
            Assert.Equal<int>(expected5, actual5);
            Assert.Equal(expected6, actual6);
            Assert.Equal<int>(expected7, actual7);

        }      
       

    }         
}