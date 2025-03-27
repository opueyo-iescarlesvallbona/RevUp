using System;
using System.Collections.Generic;

namespace RevUp_API_server.Models;

public partial class MemberRelation
{
    public int MemberId1 { get; set; }

    public int MemberId2 { get; set; }

    public int StateId { get; set; }

    public virtual Member MemberId1Navigation { get; set; } = null!;

    public virtual Member MemberId2Navigation { get; set; } = null!;

    public virtual RelationState1 State { get; set; } = null!;
}
