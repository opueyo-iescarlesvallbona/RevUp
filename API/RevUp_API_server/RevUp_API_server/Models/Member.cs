using System;
using System.Collections.Generic;

namespace RevUp_API_server.Models;

public partial class Member
{
    public int id { get; set; }

    public string name { get; set; } = null!;

    public string? membername { get; set; }

    public int? experience { get; set; }

    public string email { get; set; } = null!;

    public int gender_id { get; set; }

    public int location_id { get; set; }

    public DateOnly date_of_birth { get; set; }

    public DateOnly login_date { get; set; }

    public string? description { get; set; }

    public byte[]? profile_picture { get; set; }

    public string password { get; set; } = null!;

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
