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
    public class CommentController : ControllerBase
    {
        private ICommentService _commentService = new CommentServiceEF();

        // GET: api/Comment
        [HttpGet]
        public IEnumerable<Comment> Get()
        {
            return _commentService.GetComments();
        }

        // POST: api/Comment
        [HttpPost]
        public void Post([FromBody] Comment comment)
        {
            _commentService.AddComment(comment);
        }
    }
}
