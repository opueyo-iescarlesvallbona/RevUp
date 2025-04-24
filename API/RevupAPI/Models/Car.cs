using System;
using System.Collections.Generic;

namespace RevupAPI.Models;

public partial class Car
{
    public int Id { get; set; }

    public int MemberId { get; set; }

    public int ModelId { get; set; }

    public int? ModelYear { get; set; }

    public double? HorsePower { get; set; }

    public string? Description { get; set; }

    public string? Picture { get; set; }

    public virtual Member Member { get; set; } = null!;

    public virtual Model Model { get; set; } = null!;
}
