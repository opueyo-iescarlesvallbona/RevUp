using System;
using System.Collections.Generic;

namespace RevupAPI.Models;

public partial class MemberLocation
{
    public int Id { get; set; }

    public string Municipality { get; set; } = null!;

    public string? Ccaa { get; set; }

    public string Country { get; set; } = null!;

    public double Latitude { get; set; }

    public double Longitude { get; set; }

    public virtual ICollection<Member> Members { get; set; } = new List<Member>();
}
