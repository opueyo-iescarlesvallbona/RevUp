using System;
using System.Collections.Generic;

namespace RevUp_API_server.Models;

public partial class RelationState1
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public virtual ICollection<MemberRelation> MemberRelations { get; set; } = new List<MemberRelation>();
}
