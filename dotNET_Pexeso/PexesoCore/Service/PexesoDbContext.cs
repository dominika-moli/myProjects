using System;
using System.Collections.Generic;
using System.Text;
using Microsoft.EntityFrameworkCore;
using PexesoGame.Entity;

namespace PexesoGame.Service
{
    public class PexesoDbContext : DbContext
    {
        public DbSet<Score> Scores { get; set; }
        public DbSet<Comment> Comments { get; set; }
        public DbSet<Rating> Ratings { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)        
            => optionsBuilder.UseSqlite("Filename=../pexeso.db");        
    }
}
