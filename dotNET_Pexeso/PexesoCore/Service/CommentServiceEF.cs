using System.Collections.Generic;
using System.Linq;
using Microsoft.EntityFrameworkCore;
using PexesoGame.Entity;

namespace PexesoGame.Service
{
    public class CommentServiceEF : ICommentService
    {
        public void AddComment(Comment comment)
        {
            using (var context = new PexesoDbContext())
            {
                context.Comments.Add(comment);
                context.SaveChanges();
            }
        }

        public IList<Comment> GetComments()
        {
            using (var context = new PexesoDbContext())
            {
                return (from c in context.Comments select c).ToList();
            }
        }

        public void ClearComments()
        {
            using (var context = new PexesoDbContext())
            {
                context.Database.ExecuteSqlRaw("DELETE FROM Comments");
            }
        }
    }
}
