using System;
using System.Collections.Generic;

namespace RevUp_API_server.Models;

public partial class Gender
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public virtual ICollection<Member> Members { get; set; } = new List<Member>();
}
