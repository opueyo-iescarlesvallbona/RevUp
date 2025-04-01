using System;
using System.Collections.Generic;

namespace RevupAPI.Models;

public partial class Car
{
    public int Id { get; set; }

    public string Brand { get; set; } = null!;

    public string Model { get; set; } = null!;

    public int ModelYear { get; set; }

    public string? Description { get; set; }

    public int MemberId { get; set; }

    public virtual Member Member { get; set; } = null!;
}
