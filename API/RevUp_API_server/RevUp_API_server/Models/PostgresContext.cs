using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;

namespace RevUp_API_server.Models;

public partial class PostgresContext : DbContext
{
    

    public PostgresContext(DbContextOptions<PostgresContext> options) : base(options) { }

    public virtual DbSet<Car> Cars { get; set; }

    public virtual DbSet<Club> Clubs { get; set; }

    public virtual DbSet<ClubEvent> ClubEvents { get; set; }

    public virtual DbSet<Gender> Genders { get; set; }

    public virtual DbSet<Member> Members { get; set; }

    public virtual DbSet<MemberClub> MemberClubs { get; set; }

    public virtual DbSet<MemberClubRole> MemberClubRoles { get; set; }

    public virtual DbSet<MemberLocation> MemberLocations { get; set; }

    public virtual DbSet<MemberRelation> MemberRelations { get; set; }

    public virtual DbSet<Message> Messages { get; set; }

    public virtual DbSet<MessageState> MessageStates { get; set; }

    public virtual DbSet<MessageState1> MessageStates1 { get; set; }

    public virtual DbSet<Post> Posts { get; set; }

    public virtual DbSet<PostComment> PostComments { get; set; }

    public virtual DbSet<PostType> PostTypes { get; set; }

    public virtual DbSet<RelationState> RelationStates { get; set; }

    public virtual DbSet<RelationState1> RelationStates1 { get; set; }

    public virtual DbSet<Route> Routes { get; set; }

    public virtual DbSet<TerrainType> TerrainTypes { get; set; }

    public virtual DbSet<TerrainType1> TerrainTypes1 { get; set; }

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        => optionsBuilder.UseNpgsql("Host=localhost;Database=postgres;Username=postgres;Password=RevUpFounders26");

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Car>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("car_pkey");

            entity.ToTable("car", "revup");

            entity.Property(e => e.Id)
                .HasDefaultValueSql("nextval('car_id_seq'::regclass)")
                .HasColumnName("id");
            entity.Property(e => e.Brand)
                .HasMaxLength(100)
                .HasColumnName("brand");
            entity.Property(e => e.Description).HasColumnName("description");
            entity.Property(e => e.MemberId).HasColumnName("member_id");
            entity.Property(e => e.Model)
                .HasMaxLength(200)
                .HasColumnName("model");
            entity.Property(e => e.ModelYear).HasColumnName("model_year");

            entity.HasOne(d => d.Member).WithMany(p => p.Cars)
                .HasForeignKey(d => d.MemberId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_car_member");
        });

        modelBuilder.Entity<Club>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("club_pkey");

            entity.ToTable("club", "revup");

            entity.Property(e => e.Id)
                .HasDefaultValueSql("nextval('club_id_seq'::regclass)")
                .HasColumnName("id");
            entity.Property(e => e.Description).HasColumnName("description");
            entity.Property(e => e.Founder).HasColumnName("founder");
            entity.Property(e => e.Name)
                .HasMaxLength(100)
                .HasColumnName("name");
            entity.Property(e => e.Picture).HasColumnName("picture");

            entity.HasOne(d => d.FounderNavigation).WithMany(p => p.Clubs)
                .HasForeignKey(d => d.Founder)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_club_member");
        });

        modelBuilder.Entity<ClubEvent>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("club_event_pkey");

            entity.ToTable("club_event", "revup");

            entity.Property(e => e.Id)
                .HasDefaultValueSql("nextval('club_event_id_seq'::regclass)")
                .HasColumnName("id");
            entity.Property(e => e.Address)
                .HasMaxLength(255)
                .HasColumnName("address");
            entity.Property(e => e.ClubId).HasColumnName("club_id");
            entity.Property(e => e.Description).HasColumnName("description");
            entity.Property(e => e.EndDate)
                .HasColumnType("timestamp without time zone")
                .HasColumnName("end_date");
            entity.Property(e => e.Name)
                .HasMaxLength(100)
                .HasColumnName("name");
            entity.Property(e => e.Picture).HasColumnName("picture");
            entity.Property(e => e.RouteStartDate)
                .HasColumnType("timestamp without time zone")
                .HasColumnName("route_start_date");
            entity.Property(e => e.StartDate)
                .HasColumnType("timestamp without time zone")
                .HasColumnName("start_date");

            entity.HasOne(d => d.Club).WithMany(p => p.ClubEvents)
                .HasForeignKey(d => d.ClubId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_event_clubevent");
        });

        modelBuilder.Entity<Gender>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("gender_pkey");

            entity.ToTable("gender", "revup");

            entity.Property(e => e.Id)
                .ValueGeneratedNever()
                .HasColumnName("id");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .HasColumnName("name");
        });

        modelBuilder.Entity<Member>(entity =>
        {
            entity.HasKey(e => e.id).HasName("member_pkey");

            entity.ToTable("member", "revup");

            entity.Property(e => e.id)
                .HasDefaultValueSql("nextval('member_id_seq'::regclass)")
                .HasColumnName("id");
            entity.Property(e => e.date_of_birth).HasColumnName("date_of_birth");
            entity.Property(e => e.description).HasColumnName("description");
            entity.Property(e => e.email)
                .HasMaxLength(100)
                .HasColumnName("email");
            entity.Property(e => e.experience).HasColumnName("experience");
            entity.Property(e => e.gender_id).HasColumnName("gender_id");
            entity.Property(e => e.location_id).HasColumnName("location_id");
            entity.Property(e => e.login_date).HasColumnName("login_date");
            entity.Property(e => e.membername)
                .HasMaxLength(50)
                .HasColumnName("membername");
            entity.Property(e => e.name)
                .HasMaxLength(100)
                .HasColumnName("name");
            entity.Property(e => e.password).HasColumnName("password");
            entity.Property(e => e.profile_picture).HasColumnName("profile_picture");

            entity.HasOne(d => d.Gender).WithMany(p => p.Members)
                .HasForeignKey(d => d.gender_id)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_member_gender");

            entity.HasOne(d => d.Location).WithMany(p => p.Members)
                .HasForeignKey(d => d.location_id)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_member_memberlocation");
        });

        modelBuilder.Entity<MemberClub>(entity =>
        {
            entity.HasKey(e => new { e.MemberId, e.ClubId }).HasName("member_club_pkey");

            entity.ToTable("member_club", "revup");

            entity.Property(e => e.MemberId).HasColumnName("member_id");
            entity.Property(e => e.ClubId).HasColumnName("club_id");
            entity.Property(e => e.JoinDate)
                .HasDefaultValueSql("now()")
                .HasColumnName("join_date");
            entity.Property(e => e.RoleType).HasColumnName("role_type");

            entity.HasOne(d => d.Club).WithMany(p => p.MemberClubs)
                .HasForeignKey(d => d.ClubId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_memberclub_club");

            entity.HasOne(d => d.Member).WithMany(p => p.MemberClubs)
                .HasForeignKey(d => d.MemberId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_memberclub_member");

            entity.HasOne(d => d.RoleTypeNavigation).WithMany(p => p.MemberClubs)
                .HasForeignKey(d => d.RoleType)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_memberclub_memberclubrole");
        });

        modelBuilder.Entity<MemberClubRole>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("member_club_role_pkey");

            entity.ToTable("member_club_role", "revup");

            entity.Property(e => e.Id)
                .ValueGeneratedNever()
                .HasColumnName("id");
            entity.Property(e => e.Name)
                .HasMaxLength(100)
                .HasColumnName("name");
        });

        modelBuilder.Entity<MemberLocation>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("member_location_pkey");

            entity.ToTable("member_location", "revup");

            entity.Property(e => e.Id)
                .HasDefaultValueSql("nextval('member_location_id_seq'::regclass)")
                .HasColumnName("id");
            entity.Property(e => e.Ccaa)
                .HasMaxLength(100)
                .HasColumnName("ccaa");
            entity.Property(e => e.Country)
                .HasMaxLength(100)
                .HasColumnName("country");
            entity.Property(e => e.Latitude).HasColumnName("latitude");
            entity.Property(e => e.Longitude).HasColumnName("longitude");
            entity.Property(e => e.Municipality)
                .HasMaxLength(200)
                .HasColumnName("municipality");
        });

        modelBuilder.Entity<MemberRelation>(entity =>
        {
            entity.HasKey(e => new { e.MemberId1, e.MemberId2 }).HasName("member_relation_pkey");

            entity.ToTable("member_relation", "revup");

            entity.Property(e => e.MemberId1).HasColumnName("member_id1");
            entity.Property(e => e.MemberId2).HasColumnName("member_id2");
            entity.Property(e => e.StateId).HasColumnName("state_id");

            entity.HasOne(d => d.MemberId1Navigation).WithMany(p => p.MemberRelationMemberId1Navigations)
                .HasForeignKey(d => d.MemberId1)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_memberrelation_member1");

            entity.HasOne(d => d.MemberId2Navigation).WithMany(p => p.MemberRelationMemberId2Navigations)
                .HasForeignKey(d => d.MemberId2)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_memberrelation_member2");

            entity.HasOne(d => d.State).WithMany(p => p.MemberRelations)
                .HasForeignKey(d => d.StateId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_memberrelation_relationstate");
        });

        modelBuilder.Entity<Message>(entity =>
        {
            entity.HasKey(e => new { e.SenderId, e.ReceiverId, e.Datetime }).HasName("message_pkey");

            entity.ToTable("message", "revup");

            entity.Property(e => e.SenderId).HasColumnName("sender_id");
            entity.Property(e => e.ReceiverId).HasColumnName("receiver_id");
            entity.Property(e => e.Datetime)
                .HasColumnType("timestamp without time zone")
                .HasColumnName("datetime");
            entity.Property(e => e.ContentMessage).HasColumnName("content_message");
            entity.Property(e => e.StateId).HasColumnName("state_id");

            entity.HasOne(d => d.Receiver).WithMany(p => p.MessageReceivers)
                .HasForeignKey(d => d.ReceiverId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_message_member2");

            entity.HasOne(d => d.Sender).WithMany(p => p.MessageSenders)
                .HasForeignKey(d => d.SenderId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_message_member1");

            entity.HasOne(d => d.State).WithMany(p => p.Messages)
                .HasForeignKey(d => d.StateId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_message_messagestate");
        });

        modelBuilder.Entity<MessageState>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("message_state_pkey");

            entity.ToTable("message_state", "revup");

            entity.Property(e => e.Id)
                .ValueGeneratedNever()
                .HasColumnName("id");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .HasColumnName("name");
        });

        modelBuilder.Entity<MessageState1>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("message_state_pkey");

            entity.ToTable("message_state");

            entity.Property(e => e.Id)
                .ValueGeneratedNever()
                .HasColumnName("id");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .HasColumnName("name");
        });

        modelBuilder.Entity<Post>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("post_pkey");

            entity.ToTable("post", "revup");

            entity.Property(e => e.Id)
                .HasDefaultValueSql("nextval('post_id_seq'::regclass)")
                .HasColumnName("id");
            entity.Property(e => e.Address)
                .HasMaxLength(100)
                .HasColumnName("address");
            entity.Property(e => e.Description).HasColumnName("description");
            entity.Property(e => e.Likes).HasColumnName("likes");
            entity.Property(e => e.MemberId).HasColumnName("member_id");
            entity.Property(e => e.Picture).HasColumnName("picture");
            entity.Property(e => e.PostDate)
                .HasColumnType("timestamp without time zone")
                .HasColumnName("post_date");
            entity.Property(e => e.PostType).HasColumnName("post_type");
            entity.Property(e => e.RouteId).HasColumnName("route_id");
            entity.Property(e => e.Title)
                .HasMaxLength(200)
                .HasColumnName("title");

            entity.HasOne(d => d.Member).WithMany(p => p.Posts)
                .HasForeignKey(d => d.MemberId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_post_member");

            entity.HasOne(d => d.Route).WithMany(p => p.Posts)
                .HasForeignKey(d => d.RouteId)
                .HasConstraintName("fk_post_route");
        });

        modelBuilder.Entity<PostComment>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("post_comment_pkey");

            entity.ToTable("post_comment", "revup");

            entity.Property(e => e.Id)
                .HasDefaultValueSql("nextval('post_comment_id_seq'::regclass)")
                .HasColumnName("id");
            entity.Property(e => e.CommentContent).HasColumnName("comment_content");
            entity.Property(e => e.Datetime)
                .HasColumnType("timestamp without time zone")
                .HasColumnName("datetime");
            entity.Property(e => e.MemberId).HasColumnName("member_id");
            entity.Property(e => e.PostId).HasColumnName("post_id");

            entity.HasOne(d => d.Post).WithMany(p => p.PostComments)
                .HasForeignKey(d => d.PostId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_postcomment_post");
        });

        modelBuilder.Entity<PostType>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("post_type_pkey");

            entity.ToTable("post_type", "revup");

            entity.Property(e => e.Id)
                .ValueGeneratedNever()
                .HasColumnName("id");
            entity.Property(e => e.Name)
                .HasMaxLength(100)
                .HasColumnName("name");
        });

        modelBuilder.Entity<RelationState>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("relation_state_pkey");

            entity.ToTable("relation_state");

            entity.Property(e => e.Id)
                .ValueGeneratedNever()
                .HasColumnName("id");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .HasColumnName("name");
        });

        modelBuilder.Entity<RelationState1>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("relation_state_pkey");

            entity.ToTable("relation_state", "revup");

            entity.Property(e => e.Id)
                .ValueGeneratedNever()
                .HasColumnName("id");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .HasColumnName("name");
        });

        modelBuilder.Entity<Route>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("route_pkey");

            entity.ToTable("route", "revup");

            entity.Property(e => e.Id)
                .HasDefaultValueSql("nextval('route_id_seq'::regclass)")
                .HasColumnName("id");
            entity.Property(e => e.Description).HasColumnName("description");
            entity.Property(e => e.Distance)
                .HasPrecision(12, 2)
                .HasColumnName("distance");
            entity.Property(e => e.Duration).HasColumnName("duration");
            entity.Property(e => e.ElevationGain)
                .HasPrecision(6, 2)
                .HasColumnName("elevation_gain");
            entity.Property(e => e.EndAddress)
                .HasMaxLength(200)
                .HasColumnName("end_address");
            entity.Property(e => e.MaxElevation)
                .HasPrecision(6, 2)
                .HasColumnName("max_elevation");
            entity.Property(e => e.MemberId).HasColumnName("member_id");
            entity.Property(e => e.Name)
                .HasMaxLength(100)
                .HasDefaultValueSql("now()")
                .HasColumnName("name");
            entity.Property(e => e.StartAddress)
                .HasMaxLength(200)
                .HasColumnName("start_address");
            entity.Property(e => e.TerrainTypeId).HasColumnName("terrain_type_id");
            entity.Property(e => e.Waypoints)
                .HasColumnType("json")
                .HasColumnName("waypoints");

            entity.HasOne(d => d.Member).WithMany(p => p.Routes)
                .HasForeignKey(d => d.MemberId)
                .HasConstraintName("fk_route_member");

            entity.HasOne(d => d.TerrainType).WithMany(p => p.Routes)
                .HasForeignKey(d => d.TerrainTypeId)
                .HasConstraintName("fk_route_terraintype");
        });

        modelBuilder.Entity<TerrainType>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("terrain_type_pkey");

            entity.ToTable("terrain_type", "revup");

            entity.Property(e => e.Id)
                .ValueGeneratedNever()
                .HasColumnName("id");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .HasColumnName("name");
        });

        modelBuilder.Entity<TerrainType1>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("terrain_type_pkey");

            entity.ToTable("terrain_type");

            entity.Property(e => e.Id)
                .ValueGeneratedNever()
                .HasColumnName("id");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .HasColumnName("name");
        });

        OnModelCreatingPartial(modelBuilder);
    }

    partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
}
