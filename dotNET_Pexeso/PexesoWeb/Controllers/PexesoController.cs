using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Http;
using PexesoGame.Core;
using PexesoGame.Entity;
using PexesoGame.Models;
using PexesoGame.Service;

namespace PexesoGame.Controllers
{
    public class PexesoController : Controller
    {
        IScoreService _scoreService = new ScoreServiceEF();
        ICommentService _commentService = new CommentServiceEF();
        IRatingService _ratingService = new RatingServiceEF();

        public IActionResult Index(int hra, int row, int column, int close, int hint, int size, string level, string pname, string comment, int rating)
        {
            ViewBag.StavHry = hra;
            ViewBag.Row = row;
            ViewBag.Column = column;
            ViewBag.Close = close;
            ViewBag.Hint = hint;
            ViewBag.Score = 0;
            if (size != 2 && size != 4 && size != 6 && size != 8)
            {
                ViewBag.Size = 8;
            }
            else ViewBag.Size = size;
            if (level != "traffic" && level != "flower" && level != "animal")
            {
                ViewBag.Level = "animal";
            }
            else { ViewBag.Level = level; }
            if (pname != "") { ViewBag.PName = pname; } else { ViewBag.PName = "anonymous"; }
            if (comment is null) { ViewBag.Comment = ""; }
            else { ViewBag.Comment = comment; }
            if ((rating >= 1) && (rating <= 5)) { ViewBag.Rating = rating; }
            else { ViewBag.Rating = 0; }

            if (ViewBag.StavHry == 1)
            {
                var board = new Board(ViewBag.Size, ViewBag.Size, 1);
                board.Level = ViewBag.Level;
                board.Name = ViewBag.PName;
                HttpContext.Session.SetObject("board", board);
                var model = PrepareModel1("New board created");
                return View(model);
            }
            else
            {
                if (ViewBag.StavHry == 2)
                {
                    var board = (Board)HttpContext.Session.GetObject("board");
                    if (ViewBag.Comment != "")
                    {
                        _commentService.AddComment(new Comment { Author = board.Name, Message = ViewBag.Comment });
                    }
                    if (ViewBag.Rating != 0)
                    {
                        _ratingService.AddRating(new Rating { Player = board.Name, NumberOfStars = ViewBag.Rating });
                    }
                    if (hint == 1)
                    {
                        board.Hint2();
                    }
                    else
                    {
                        if (hint == 2)
                        {
                            board.Solution();
                        }
                        else
                        {
                            if ((ViewBag.Row != -1) && (ViewBag.Column != -1))
                            {
                                board.CompareCard();
                                board.FlipCard(ViewBag.Row, ViewBag.Column);

                                System.Console.WriteLine(board.MyGameState);
                                if (board.LastPair())
                                {
                                    board.MyGameState = GameState.SOLVED;

                                }
                                if (board.IsSolved())
                                {
                                    System.Console.WriteLine(board.MyGameState);
                                    _scoreService.AddScore(new Score { Player = board.Name, Points = board.GetScore() });
                                    ViewBag.Score = board.GetScore();
                                }
                            }
                        }
                    }
                    HttpContext.Session.SetObject("board", board);
                    var model = PrepareModel1("New board created");
                    return View(model);
                }
                else
                {
                    var model = PrepareModel1("Uz bezi");
                    return View(model);
                }
            }
        }

        private PexesoModel PrepareModel1(string message)
        {
            return new PexesoModel
            {
                Board = (Board)HttpContext.Session.GetObject("board"),
                Message = message,
                Ratings = _ratingService.GetRatings(),
                AvgRating = _ratingService.GetAverageRating(),
                Comments = _commentService.GetComments(),
                Scores = _scoreService.GetTopScores(),

            };
        }
    }
}