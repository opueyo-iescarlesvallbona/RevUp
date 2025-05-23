﻿using System;
using System.Collections.Generic;

namespace RevupAPI.Models;

public partial class RelationState
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public virtual ICollection<MemberRelation> MemberRelations { get; set; } = new List<MemberRelation>();
}
