using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using PexesoGame.Models;
using Microsoft.AspNetCore.Http;
using PexesoGame.Core;
using PexesoGame.Entity;
using PexesoGame.Service;

namespace PexesoGame.Controllers
{
    public class HomeController : Controller
    {
        IScoreService _scoreService = new ScoreServiceEF();
        ICommentService _commentService = new CommentServiceEF();
        IRatingService _ratingService = new RatingServiceEF();


        private readonly ILogger<HomeController> _logger;

        public HomeController(ILogger<HomeController> logger)
        {
            _logger = logger;
        }

        public IActionResult Index()
        {
           // _scoreService.ClearScores();
            //_commentService.ClearComments();
           // _ratingService.ClearRatings();
           // _commentService.AddComment(new Comment{Author = "Jozko", Message = "super"});
             // _ratingService.AddRating(new Rating{Player = "Jozko", NumberOfStars = 4});
            //  _scoreService.AddScore(new Score{Player = "Jozko", Points = 320});    
            var model = PrepareModel();
            return View(model);
        }

        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
        }

        private HomeModel PrepareModel()
        {
            return new HomeModel
            {
                Ratings = _ratingService.GetRatings(),
                AvgRating = _ratingService.GetAverageRating(),
                Comments = _commentService.GetComments(),
                Scores = _scoreService.GetTopScores()

            };
        }
    }
}
