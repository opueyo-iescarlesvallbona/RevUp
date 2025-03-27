using System;
using System.Collections.Generic;

namespace RevUp_API_server.Models;

public partial class Post
{
    public int Id { get; set; }

    public string Title { get; set; } = null!;

    public int PostType { get; set; }

    public string? Description { get; set; }

    public DateTime PostDate { get; set; }

    public byte[]? Picture { get; set; }

    public long Likes { get; set; }

    public string? Address { get; set; }

    public int? RouteId { get; set; }

    public int MemberId { get; set; }

    public virtual Member Member { get; set; } = null!;

    public virtual ICollection<PostComment> PostComments { get; set; } = new List<PostComment>();

    public virtual Route? Route { get; set; }
}
