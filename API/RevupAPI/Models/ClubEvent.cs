using System;
using System.Collections.Generic;

namespace RevupAPI.Models;

public partial class ClubEvent
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public string Address { get; set; } = null!;

    public int ClubId { get; set; }

    public byte[]? Picture { get; set; }

    public DateTime StartDate { get; set; }

    public DateTime? RouteStartDate { get; set; }

    public DateTime EndDate { get; set; }

    public string? Description { get; set; }

    public virtual Club Club { get; set; } = null!;
}
