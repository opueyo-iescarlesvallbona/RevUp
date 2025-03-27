using System;
using System.Collections.Generic;

namespace RevUp_API_server.Models;

public partial class MemberClubRole
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public virtual ICollection<MemberClub> MemberClubs { get; set; } = new List<MemberClub>();
}
