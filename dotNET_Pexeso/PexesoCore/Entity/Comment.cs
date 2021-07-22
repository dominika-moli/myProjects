using System;

namespace PexesoGame.Entity
{
    [Serializable]
    public class Comment
    {
        public int Id { get; set; }

        public string Author { get; set; }

        public string Message { get; set; }
    }
}