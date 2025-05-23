﻿using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;

namespace RevupAPI.Models;

public partial class RevupContext : DbContext
{
    public RevupContext()
    {
    }

    public RevupContext(DbContextOptions<RevupContext> options)
        : base(options)
    {
    }

    public virtual DbSet<Brand> Brands { get; set; }

    public virtual DbSet<Car> Cars { get; set; }

    public virtual DbSet<Club> Clubs { get; set; }

    public virtual DbSet<ClubEvent> ClubEvents { get; set; }

    public virtual DbSet<EventState> EventStates { get; set; }

    public virtual DbSet<Gender> Genders { get; set; }

    public virtual DbSet<Member> Members { get; set; }

    public virtual DbSet<MemberClub> MemberClubs { get; set; }

    public virtual DbSet<MemberClubRole> MemberClubRoles { get; set; }

    public virtual DbSet<MemberLocation> MemberLocations { get; set; }

    public virtual DbSet<MemberRelation> MemberRelations { get; set; }

    public virtual DbSet<Message> Messages { get; set; }

    public virtual DbSet<MessageState> MessageStates { get; set; }

    public virtual DbSet<Model> Models { get; set; }

    public virtual DbSet<Post> Posts { get; set; }

    public virtual DbSet<PostComment> PostComments { get; set; }

    public virtual DbSet<PostType> PostTypes { get; set; }

    public virtual DbSet<RelationState> RelationStates { get; set; }

    public virtual DbSet<Route> Routes { get; set; }

    public virtual DbSet<TerrainType> TerrainTypes { get; set; }

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        => optionsBuilder.UseSqlServer("Server=.\\sqlexpress; Trusted_Connection=True; Encrypt=false; Database=revup");

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Brand>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__brand__3213E83FCE8F4ECF");

            entity.ToTable("brand");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Name)
                .HasMaxLength(100)
                .IsUnicode(false)
                .HasColumnName("name");
        });

        modelBuilder.Entity<Car>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__car__3213E83F0ABD7505");

            entity.ToTable("car");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Description)
                .HasColumnType("text")
                .HasColumnName("description");
            entity.Property(e => e.HorsePower).HasColumnName("horse_power");
            entity.Property(e => e.MemberId).HasColumnName("member_id");
            entity.Property(e => e.ModelId).HasColumnName("model_id");
            entity.Property(e => e.ModelYear).HasColumnName("model_year");
            entity.Property(e => e.Picture)
                .HasMaxLength(500)
                .IsUnicode(false)
                .HasColumnName("picture");

            entity.HasOne(d => d.Member).WithMany(p => p.Cars)
                .HasForeignKey(d => d.MemberId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__car__member_id__756D6ECB");

            entity.HasOne(d => d.Model).WithMany(p => p.Cars)
                .HasForeignKey(d => d.ModelId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__car__model_id__76619304");
        });

        modelBuilder.Entity<Club>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__club__3213E83F42FF2E74");

            entity.ToTable("club", tb => tb.HasTrigger("club_trigger"));

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.CreationDate)
                .HasDefaultValueSql("(getdate())")
                .HasColumnName("creation_date");
            entity.Property(e => e.Description)
                .HasColumnType("text")
                .HasColumnName("description");
            entity.Property(e => e.Founder).HasColumnName("founder");
            entity.Property(e => e.Name)
                .HasMaxLength(100)
                .IsUnicode(false)
                .HasColumnName("name");
            entity.Property(e => e.Picture)
                .HasMaxLength(500)
                .IsUnicode(false)
                .HasColumnName("picture");

            entity.HasOne(d => d.FounderNavigation).WithMany(p => p.Clubs)
                .HasForeignKey(d => d.Founder)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__club__founder__5BE2A6F2");
        });

        modelBuilder.Entity<ClubEvent>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__club_eve__3213E83F04E6C6CE");

            entity.ToTable("club_event");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Address)
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasColumnName("address");
            entity.Property(e => e.ClubId).HasColumnName("club_id");
            entity.Property(e => e.Description)
                .HasColumnType("text")
                .HasColumnName("description");
            entity.Property(e => e.EndDate)
                .HasColumnType("datetime")
                .HasColumnName("end_date");
            entity.Property(e => e.Name)
                .HasMaxLength(100)
                .IsUnicode(false)
                .HasColumnName("name");
            entity.Property(e => e.Picture)
                .HasMaxLength(500)
                .IsUnicode(false)
                .HasColumnName("picture");
            entity.Property(e => e.RouteStartDate)
                .HasColumnType("datetime")
                .HasColumnName("route_start_date");
            entity.Property(e => e.StartDate)
                .HasColumnType("datetime")
                .HasColumnName("start_date");
            entity.Property(e => e.State).HasColumnName("state");
            entity.Property(e => e.Lat).HasColumnName("lat");
            entity.Property(e => e.Long).HasColumnName("long");

            entity.HasOne(d => d.Club).WithMany(p => p.ClubEvents)
                .HasForeignKey(d => d.ClubId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__club_even__club___693CA210");

            entity.HasOne(d => d.StateNavigation).WithMany(p => p.ClubEvents)
                .HasForeignKey(d => d.State)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__club_even__state__531856C7");
        });

        modelBuilder.Entity<EventState>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__event_st__3213E83F31964653");

            entity.ToTable("event_state");

            entity.Property(e => e.Id)
                .ValueGeneratedNever()
                .HasColumnName("id");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("name");
        });

        modelBuilder.Entity<Gender>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__gender__3213E83F4940534E");

            entity.ToTable("gender");

            entity.Property(e => e.Id)
                .ValueGeneratedNever()
                .HasColumnName("id");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("name");
        });

        modelBuilder.Entity<Member>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__member__3213E83F24B7DD59");

            entity.ToTable("member");

            entity.HasIndex(e => e.Membername, "member_membername").IsUnique();

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.DateOfBirth).HasColumnName("date_of_birth");
            entity.Property(e => e.Description)
                .HasColumnType("text")
                .HasColumnName("description");
            entity.Property(e => e.Email)
                .HasMaxLength(100)
                .IsUnicode(false)
                .HasColumnName("email");
            entity.Property(e => e.Experience).HasColumnName("experience");
            entity.Property(e => e.GenderId).HasColumnName("gender_id");
            entity.Property(e => e.LocationId).HasColumnName("location_id");
            entity.Property(e => e.LoginDate).HasColumnName("login_date");
            entity.Property(e => e.Membername)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("membername");
            entity.Property(e => e.Name)
                .HasMaxLength(100)
                .IsUnicode(false)
                .HasColumnName("name");
            entity.Property(e => e.Password)
                .HasColumnType("text")
                .HasColumnName("password");
            entity.Property(e => e.ProfilePicture)
                .HasMaxLength(500)
                .IsUnicode(false)
                .HasColumnName("profile_picture");

            entity.HasOne(d => d.Gender).WithMany(p => p.Members)
                .HasForeignKey(d => d.GenderId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__member__gender_i__534D60F1");

            entity.HasOne(d => d.Location).WithMany(p => p.Members)
                .HasForeignKey(d => d.LocationId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__member__location__5441852A");
        });

        modelBuilder.Entity<MemberClub>(entity =>
        {
            entity.HasKey(e => new { e.MemberId, e.ClubId }).HasName("PK__member_c__395156E92E9297B5");

            entity.ToTable("member_club");

            entity.Property(e => e.MemberId).HasColumnName("member_id");
            entity.Property(e => e.ClubId).HasColumnName("club_id");
            entity.Property(e => e.JoinDate)
                .HasDefaultValueSql("(getdate())")
                .HasColumnName("join_date");
            entity.Property(e => e.RoleType).HasColumnName("role_type");

            entity.HasOne(d => d.Club).WithMany(p => p.MemberClubs)
                .HasForeignKey(d => d.ClubId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__member_cl__club___6477ECF3");

            entity.HasOne(d => d.Member).WithMany(p => p.MemberClubs)
                .HasForeignKey(d => d.MemberId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__member_cl__membe__656C112C");

            entity.HasOne(d => d.RoleTypeNavigation).WithMany(p => p.MemberClubs)
                .HasForeignKey(d => d.RoleType)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__member_cl__role___66603565");
        });

        modelBuilder.Entity<MemberClubRole>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__member_c__3213E83F0C166BAB");

            entity.ToTable("member_club_role");

            entity.Property(e => e.Id)
                .ValueGeneratedNever()
                .HasColumnName("id");
            entity.Property(e => e.Name)
                .HasMaxLength(100)
                .IsUnicode(false)
                .HasColumnName("name");
        });

        modelBuilder.Entity<MemberLocation>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__member_l__3213E83FDC32404D");

            entity.ToTable("member_location");

            entity.Property(e => e.Id)
                .ValueGeneratedNever()
                .HasColumnName("id");
            entity.Property(e => e.Ccaa)
                .HasMaxLength(100)
                .IsUnicode(false)
                .HasColumnName("ccaa");
            entity.Property(e => e.Country)
                .HasMaxLength(100)
                .IsUnicode(false)
                .HasColumnName("country");
            entity.Property(e => e.Latitude).HasColumnName("latitude");
            entity.Property(e => e.Longitude).HasColumnName("longitude");
            entity.Property(e => e.Municipality)
                .HasMaxLength(200)
                .IsUnicode(false)
                .HasColumnName("municipality");
        });

        modelBuilder.Entity<MemberRelation>(entity =>
        {
            entity.HasKey(e => new { e.MemberId1, e.MemberId2 }).HasName("PK__member_r__40371E784442043E");

            entity.ToTable("member_relation");

            entity.Property(e => e.MemberId1).HasColumnName("member_id1");
            entity.Property(e => e.MemberId2).HasColumnName("member_id2");
            entity.Property(e => e.StateId).HasColumnName("state_id");

            entity.HasOne(d => d.MemberId1Navigation).WithMany(p => p.MemberRelationMemberId1Navigations)
                .HasForeignKey(d => d.MemberId1)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__member_re__membe__04E4BC85");

            entity.HasOne(d => d.MemberId2Navigation).WithMany(p => p.MemberRelationMemberId2Navigations)
                .HasForeignKey(d => d.MemberId2)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__member_re__membe__05D8E0BE");

            entity.HasOne(d => d.State).WithMany(p => p.MemberRelations)
                .HasForeignKey(d => d.StateId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__member_re__state__06CD04F7");
        });

        modelBuilder.Entity<Message>(entity =>
        {
            entity.HasKey(e => new { e.SenderId, e.ReceiverId, e.Datetime }).HasName("PK__message__2C54A2C421F3026D");

            entity.ToTable("message");

            entity.Property(e => e.SenderId).HasColumnName("sender_id");
            entity.Property(e => e.ReceiverId).HasColumnName("receiver_id");
            entity.Property(e => e.Datetime)
                .HasColumnType("datetime")
                .HasColumnName("datetime");
            entity.Property(e => e.ContentMessage)
                .HasColumnType("text")
                .HasColumnName("content_message");
            entity.Property(e => e.StateId).HasColumnName("state_id");

            entity.HasOne(d => d.Receiver).WithMany(p => p.MessageReceivers)
                .HasForeignKey(d => d.ReceiverId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__message__receive__10566F31");

            entity.HasOne(d => d.Sender).WithMany(p => p.MessageSenders)
                .HasForeignKey(d => d.SenderId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__message__sender___0F624AF8");

            entity.HasOne(d => d.State).WithMany(p => p.Messages)
                .HasForeignKey(d => d.StateId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__message__state_i__114A936A");
        });

        modelBuilder.Entity<MessageState>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__message___3213E83F9A737939");

            entity.ToTable("message_state");

            entity.Property(e => e.Id)
                .ValueGeneratedNever()
                .HasColumnName("id");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("name");
        });

        modelBuilder.Entity<Model>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__model__3213E83FCEA6F1D9");

            entity.ToTable("model");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.IdBrand).HasColumnName("id_brand");
            entity.Property(e => e.ModelName)
                .HasMaxLength(200)
                .IsUnicode(false)
                .HasColumnName("model_name");

            entity.HasOne(d => d.IdBrandNavigation).WithMany(p => p.Models)
                .HasForeignKey(d => d.IdBrand)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__model__id_brand__44CA3770");
        });

        modelBuilder.Entity<Post>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__post__3213E83F7CCD8E54");

            entity.ToTable("post");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Comments).HasColumnName("comments");
            entity.Property(e => e.Description)
                .HasColumnType("text")
                .HasColumnName("description");
            entity.Property(e => e.Likes).HasColumnName("likes");
            entity.Property(e => e.MemberId).HasColumnName("member_id");
            entity.Property(e => e.Picture)
                .HasMaxLength(500)
                .IsUnicode(false)
                .HasColumnName("picture");
            entity.Property(e => e.PostDate)
                .HasColumnType("datetime")
                .HasColumnName("post_date");
            entity.Property(e => e.PostType).HasColumnName("post_type");
            entity.Property(e => e.RouteId).HasColumnName("route_id");
            entity.Property(e => e.Title)
                .HasMaxLength(200)
                .IsUnicode(false)
                .HasColumnName("title");
            entity.Property(e => e.LocationId)
                .HasMaxLength(100)
                .IsUnicode(false)
                .HasColumnName("location_id");

            entity.HasOne(d => d.Member).WithMany(p => p.Posts)
                .HasForeignKey(d => d.MemberId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__post__member_id__787EE5A0");

            entity.HasOne(d => d.PostTypeNavigation).WithMany(p => p.Posts)
                .HasForeignKey(d => d.PostType)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__post__post_type__1EA48E88");

            entity.HasOne(d => d.Route).WithMany(p => p.Posts)
                .HasForeignKey(d => d.RouteId)
                .HasConstraintName("FK__post__route_id__797309D9");
            entity.HasOne(d => d.Location).WithMany(p => p.Posts)
                .HasForeignKey(d => d.LocationId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__post__location_i__19AACF41");

            entity.HasMany(d => d.Members).WithMany(p => p.PostsNavigation)
                .UsingEntity<Dictionary<string, object>>(
                    "PostMemberLike",
                    r => r.HasOne<Member>().WithMany()
                        .HasForeignKey("MemberId")
                        .OnDelete(DeleteBehavior.ClientSetNull)
                        .HasConstraintName("FK__post_memb__membe__15DA3E5D"),
                    l => l.HasOne<Post>().WithMany()
                        .HasForeignKey("PostId")
                        .OnDelete(DeleteBehavior.ClientSetNull)
                        .HasConstraintName("FK__post_memb__post___16CE6296"),
                    j =>
                    {
                        j.HasKey("PostId", "MemberId").HasName("PK__post_mem__65FE3F35A0EB6CA3");
                        j.ToTable("post_member_like", tb => tb.HasTrigger("after_insert_post_member_like"));
                        j.IndexerProperty<int>("PostId").HasColumnName("post_id");
                        j.IndexerProperty<int>("MemberId").HasColumnName("member_id");
                    });
        });

        modelBuilder.Entity<PostComment>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__post_com__3213E83F84427A89");

            entity.ToTable("post_comment", tb => tb.HasTrigger("after_insert_post_comment"));

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.CommentContent)
                .HasColumnType("text")
                .HasColumnName("comment_content");
            entity.Property(e => e.Datetime)
                .HasColumnType("datetime")
                .HasColumnName("datetime");
            entity.Property(e => e.MemberId).HasColumnName("member_id");
            entity.Property(e => e.PostId).HasColumnName("post_id");

            entity.HasOne(d => d.Member).WithMany(p => p.PostComments)
                .HasForeignKey(d => d.MemberId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__post_comm__membe__0697FACD");

            entity.HasOne(d => d.Post).WithMany(p => p.PostComments)
                .HasForeignKey(d => d.PostId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__post_comm__post___7C4F7684");
        });

        modelBuilder.Entity<PostType>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__post_typ__3213E83F912F0248");

            entity.ToTable("post_type");

            entity.Property(e => e.Id)
                .ValueGeneratedNever()
                .HasColumnName("id");
            entity.Property(e => e.Name)
                .HasMaxLength(100)
                .IsUnicode(false)
                .HasColumnName("name");
        });

        modelBuilder.Entity<RelationState>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__relation__3213E83FD0043AED");

            entity.ToTable("relation_state");

            entity.Property(e => e.Id)
                .ValueGeneratedNever()
                .HasColumnName("id");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("name");
        });

        modelBuilder.Entity<Route>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__route__3213E83F550C45DB");

            entity.ToTable("route");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Datetime)
                .HasColumnType("datetime")
                .HasColumnName("datetime");
            entity.Property(e => e.Description)
                .HasColumnType("text")
                .HasColumnName("description");
            entity.Property(e => e.Distance)
                .HasColumnType("numeric(12, 2)")
                .HasColumnName("distance");
            entity.Property(e => e.Duration).HasColumnName("duration");
            entity.Property(e => e.ElevationGain)
                .HasColumnType("numeric(6, 2)")
                .HasColumnName("elevation_gain");
            entity.Property(e => e.EndAddress)
                .HasMaxLength(200)
                .IsUnicode(false)
                .HasColumnName("end_address");
            entity.Property(e => e.MaxElevation)
                .HasColumnType("numeric(6, 2)")
                .HasColumnName("max_elevation");
            entity.Property(e => e.MemberId).HasColumnName("member_id");
            entity.Property(e => e.Name)
                .HasMaxLength(100)
                .IsUnicode(false)
                .HasDefaultValueSql("(getdate())")
                .HasColumnName("name");
            entity.Property(e => e.StartAddress)
                .HasMaxLength(200)
                .IsUnicode(false)
                .HasColumnName("start_address");
            entity.Property(e => e.TerrainTypeId).HasColumnName("terrain_type_id");
            entity.Property(e => e.Waypoints)
                .HasColumnType("text")
                .HasColumnName("waypoints");

            entity.HasOne(d => d.Member).WithMany(p => p.Routes)
                .HasForeignKey(d => d.MemberId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__route__member_id__12FDD1B2");

            entity.HasOne(d => d.TerrainType).WithMany(p => p.Routes)
                .HasForeignKey(d => d.TerrainTypeId)
                .HasConstraintName("FK__route__terrain_t__71D1E811");
        });

        modelBuilder.Entity<TerrainType>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__terrain___3213E83FB2781BD1");

            entity.ToTable("terrain_type");

            entity.Property(e => e.Id)
                .ValueGeneratedNever()
                .HasColumnName("id");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("name");
        });

        OnModelCreatingPartial(modelBuilder);
    }

    partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
}
