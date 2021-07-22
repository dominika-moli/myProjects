using PexesoGame.Entity;
using PexesoGame.Service;
using Xunit;


namespace PexesoGameTests
{
    public class RatingTest
    {
        private IRatingService RatingService()
        {
            var ratingService = new RatingServiceFile();
            ratingService.ClearRatings();
            return ratingService;
        }

        [Fact] 
         public void TestAddRating()
        {
            var ratingService = RatingService();

            ratingService.AddRating(new Rating { Player = "Janko", NumberOfStars = 3 });
            var ratings = ratingService.GetRatings();            

            var expected1 = 1;
            var actual1 = ratings.Count;

            var expected2 = "Janko";
            var actual2 = ratings[0].Player;
            var expected3 = 3;
            var actual3 = ratings[0].NumberOfStars;

            decimal expected4 = 3.0M;
            var actual4 = ratingService.GetAverageRating();
        
            Assert.Equal<int>(expected1, actual1);
            Assert.Equal(expected2, actual2);
            Assert.Equal<int>(expected3, actual3);
            Assert.Equal<decimal>(expected4, actual4);
        }

        [Fact] 
         public void TestGetRatings()
        {
            var ratingService = RatingService();

            ratingService.AddRating(new Rating {  Player = "Anicka", NumberOfStars = 1 });
            ratingService.AddRating(new Rating {  Player = "Lukas", NumberOfStars = 4 });
            ratingService.AddRating(new Rating {  Player = "Katka", NumberOfStars = 2});
            ratingService.AddRating(new Rating {  Player = "Tomas", NumberOfStars = 2 });
            ratingService.AddRating(new Rating {  Player = "Alena", NumberOfStars = 5 });

            var ratings = ratingService.GetRatings();

            var expected1 = 5;
            var actual1 = ratings.Count;

            var expected2 = "Anicka";
            var actual2 = ratings[0].Player;
            var expected3 = 1;
            var actual3 = ratings[0].NumberOfStars;


            var expected4 = "Lukas";
            var actual4 = ratings[1].Player;
            var expected5 = 4;
            var actual5 = ratings[1].NumberOfStars;


            var expected6 = "Katka";
            var actual6 = ratings[2].Player;           
            var expected7 = 2;
            var actual7 = ratings[2].NumberOfStars;

            var expected8 = "Tomas";
            var actual8 = ratings[3].Player;           
            var expected9 = 2;
            var actual9 = ratings[3].NumberOfStars;

            var expected10 = "Alena";
            var actual10 = ratings[4].Player;           
            var expected11 = 5;
            var actual11 = ratings[4].NumberOfStars;

            decimal expected12 = 2.80M;
            var actual12 = ratingService.GetAverageRating();

        
            Assert.Equal<int>(expected1, actual1);

            Assert.Equal(expected2, actual2);
            Assert.Equal(expected3, actual3);
            Assert.Equal(expected4, actual4);
            Assert.Equal(expected5, actual5);
            Assert.Equal(expected6, actual6);
            Assert.Equal(expected7, actual7);
            Assert.Equal(expected8, actual8);
            Assert.Equal(expected9, actual9);
            Assert.Equal(expected10, actual10);
            Assert.Equal(expected11, actual11);
            Assert.Equal<decimal>(expected12, actual12);
        }   
            
     }         
}