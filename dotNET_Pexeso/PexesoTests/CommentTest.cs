using PexesoGame.Entity;
using PexesoGame.Service;
using Xunit;


namespace PexesoGameTests
{
    public class CommentTest
    {
        Comment comment = new Comment();
        private ICommentService CommentService()
        {
            var commentService = new CommentServiceFile();
            commentService.ClearComments();
            return commentService;
        }

        [Fact] 
         public void TestAddComment()
        {
            var commentService = CommentService();

            commentService.AddComment(new Comment { Author = "Mirka", Message = "hra sa mi pacila" });
            var comments = commentService.GetComments();

            var expected1 = 1;
            var actual1 = comments.Count;

            var expected2 = "Mirka";
            var actual2 = comments[0].Author;

            var expected3 = "hra sa mi pacila";
            var actual3 = comments[0].Message;

        
            Assert.Equal<int>(expected1, actual1);
            Assert.Equal(expected2, actual2);
            Assert.Equal(expected3, actual3);
        }


        [Fact] 
         public void TestGetComments()
        {
            var commentService = CommentService();

            commentService.AddComment(new Comment {  Author = "Anicka", Message = "paradicka" });
            commentService.AddComment(new Comment {  Author = "Lukas", Message = "blba hra" });
            commentService.AddComment(new Comment {  Author = "Katka", Message = "zabavil som sa" });
            commentService.AddComment(new Comment {  Author = "Tomas", Message = "somarina lebo som prehral" });
            var comments = commentService.GetComments();

            var expected1 = 4;
            var actual1 = comments.Count;

            var expected2 = "Anicka";
            var actual2 = comments[0].Author;
            var expected3 = "paradicka";
            var actual3 = comments[0].Message;


            var expected4 = "Lukas";
            var actual4 = comments[1].Author;
            var expected5 = "blba hra";
            var actual5 = comments[1].Message;


            var expected6 = "Katka";
            var actual6 = comments[2].Author;           
            var expected7 = "zabavil som sa";
            var actual7 = comments[2].Message;

            var expected8 = "Tomas";
            var actual8 = comments[3].Author;           
            var expected9 = "somarina lebo som prehral";
            var actual9 = comments[3].Message;

        
            Assert.Equal<int>(expected1, actual1);

            Assert.Equal(expected2, actual2);
            Assert.Equal(expected3, actual3);
            Assert.Equal(expected4, actual4);
            Assert.Equal(expected5, actual5);
            Assert.Equal(expected6, actual6);
            Assert.Equal(expected7, actual7);
            Assert.Equal(expected8, actual8);
            Assert.Equal(expected9, actual9);
        } 

    }         
}