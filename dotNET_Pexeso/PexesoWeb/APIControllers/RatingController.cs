using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using PexesoGame.Entity;
using PexesoGame.Service;

namespace PexesoGame.APIControllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class RatingController : ControllerBase
    {
        private IRatingService _ratingService = new RatingServiceEF();

        // GET: api/Rating
        [HttpGet]
        public IEnumerable<Rating> Get()
        {
            return _ratingService.GetRatings();
        }

        // POST: api/Rating
        [HttpPost]
        public void Post([FromBody] Rating rating)
        {
            _ratingService.AddRating(rating);
        }
    }
}
