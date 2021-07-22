using System.Collections.Generic;
using PexesoGame.Entity;

namespace PexesoGame.Service
{
    public interface ICommentService
    {
        void AddComment(Comment comment);

        IList<Comment> GetComments();

        void ClearComments();
    }
}
