using System;
using System.Collections.Generic;

namespace RevupAPI.Models;

public partial class TerrainType
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public virtual ICollection<Route> Routes { get; set; } = new List<Route>();
}
