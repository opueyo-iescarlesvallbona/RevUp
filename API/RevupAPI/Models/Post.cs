using System;
using System.Collections.Generic;

namespace RevupAPI.Models;

public partial class Post
{
    public int Id { get; set; }

    public string Title { get; set; } = null!;

    public int PostType { get; set; }

    public string? Description { get; set; }

    public DateTime PostDate { get; set; }

    public string? Picture { get; set; }

    public long Likes { get; set; }

    public int? RouteId { get; set; }

    public int MemberId { get; set; }

    public long Comments { get; set; }

    public int? LocationId { get; set; }

    public virtual Member Member { get; set; } = null!;

    public virtual ICollection<PostComment> PostComments { get; set; } = new List<PostComment>();

    public virtual PostType PostTypeNavigation { get; set; } = null!;

    public virtual Route? Route { get; set; }

    public virtual ICollection<Member> Members { get; set; } = new List<Member>();
    public virtual MemberLocation? Location { get; set; }
}
