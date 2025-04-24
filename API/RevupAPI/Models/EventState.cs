using System;
using System.Collections.Generic;

namespace RevupAPI.Models;

public partial class EventState
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public virtual ICollection<ClubEvent> ClubEvents { get; set; } = new List<ClubEvent>();
}
