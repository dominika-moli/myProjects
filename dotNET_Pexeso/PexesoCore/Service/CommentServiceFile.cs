using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization.Formatters.Binary;
using PexesoGame.Entity;

namespace PexesoGame.Service
{
    public class CommentServiceFile : ICommentService
    {
        private const string FileName = "comment.bin";

        private List<Comment> comments = new List<Comment>();

        public void AddComment(Comment comment)
        {
            GetComments();

            comments.Add(comment);

            SaveComments();
        }

        public IList<Comment> GetComments()
        {
            LoadComments();

           return (from c in comments select c).ToList();
        }

        public void ClearComments()
        {
            comments.Clear();
            File.Delete(FileName);
        }

        private void SaveComments()
        {
            using (var fs = File.OpenWrite(FileName))
            {
                var bf = new BinaryFormatter();
                bf.Serialize(fs, comments);
            }
        }

        private void LoadComments()
        {
            if (File.Exists(FileName))
            {
                using (var fs = File.OpenRead(FileName))
                {
                    var bf = new BinaryFormatter();
                    comments = (List<Comment>)bf.Deserialize(fs);
                }
            }
        }

    }
}
