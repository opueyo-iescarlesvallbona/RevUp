using System;
using System.Collections.Generic;

namespace RevupAPI.Models;

public partial class Member
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public string? Membername { get; set; }

    public int? Experience { get; set; }

    public string Email { get; set; } = null!;

    public int GenderId { get; set; }

    public int LocationId { get; set; }

    public DateOnly DateOfBirth { get; set; }

    public DateOnly LoginDate { get; set; }

    public string? Description { get; set; }

    public byte[]? ProfilePicture { get; set; }

    public string Password { get; set; } = null!;

    public virtual ICollection<Car> Cars { get; set; } = new List<Car>();

    public virtual ICollection<Club> Clubs { get; set; } = new List<Club>();

    public virtual Gender Gender { get; set; } = null!;

    public virtual MemberLocation Location { get; set; } = null!;

    public virtual ICollection<MemberClub> MemberClubs { get; set; } = new List<MemberClub>();

    public virtual ICollection<MemberRelation> MemberRelationMemberId1Navigations { get; set; } = new List<MemberRelation>();

    public virtual ICollection<MemberRelation> MemberRelationMemberId2Navigations { get; set; } = new List<MemberRelation>();

    public virtual ICollection<Message> MessageReceivers { get; set; } = new List<Message>();

    public virtual ICollection<Message> MessageSenders { get; set; } = new List<Message>();

    public virtual ICollection<Post> Posts { get; set; } = new List<Post>();

    public virtual ICollection<Route> Routes { get; set; } = new List<Route>();
}
