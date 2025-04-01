using System;
using System.Collections.Generic;

namespace RevupAPI.Models;

public partial class Club
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public int Founder { get; set; }

    public string? Description { get; set; }

    public byte[]? Picture { get; set; }

    public virtual ICollection<ClubEvent> ClubEvents { get; set; } = new List<ClubEvent>();

    public virtual Member FounderNavigation { get; set; } = null!;

    public virtual ICollection<MemberClub> MemberClubs { get; set; } = new List<MemberClub>();
}
