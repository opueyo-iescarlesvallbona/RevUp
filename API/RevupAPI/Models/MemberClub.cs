﻿using System;
using System.Collections.Generic;

namespace RevupAPI.Models;

public partial class MemberClub
{
    public int MemberId { get; set; }

    public int ClubId { get; set; }

    public int RoleType { get; set; }

    public DateOnly JoinDate { get; set; }

    public virtual Club Club { get; set; } = null!;

    public virtual Member Member { get; set; } = null!;

    public virtual MemberClubRole RoleTypeNavigation { get; set; } = null!;
}
