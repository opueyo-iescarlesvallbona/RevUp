using System;
using System.Collections.Generic;

namespace RevUp_API_server.Models;

public partial class Route
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public string Waypoints { get; set; } = null!;

    public decimal Distance { get; set; }

    public long Duration { get; set; }

    public decimal? MaxElevation { get; set; }

    public decimal? ElevationGain { get; set; }

    public string StartAddress { get; set; } = null!;

    public string EndAddress { get; set; } = null!;

    public int? TerrainTypeId { get; set; }

    public string? Description { get; set; }

    public int? MemberId { get; set; }

    public virtual Member? Member { get; set; }

    public virtual ICollection<Post> Posts { get; set; } = new List<Post>();

    public virtual TerrainType? TerrainType { get; set; }
}
