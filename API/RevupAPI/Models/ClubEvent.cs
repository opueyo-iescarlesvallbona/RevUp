using System;
using System.Collections.Generic;

namespace RevupAPI.Models;

public partial class ClubEvent
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public string Address { get; set; } = null!;

    public int ClubId { get; set; }

    public string? Picture { get; set; }

    public DateTime StartDate { get; set; }

    public DateTime? RouteStartDate { get; set; }

    public DateTime EndDate { get; set; }

    public string? Description { get; set; }

    public int State { get; set; }

    public decimal Lat { get; set; }

    public decimal Long { get; set; }

    public virtual Club Club { get; set; } = null!;

    public virtual EventState StateNavigation { get; set; } = null!;
}
