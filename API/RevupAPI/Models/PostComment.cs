using System;
using System.Collections.Generic;

namespace RevupAPI.Models;

public partial class PostComment
{
    public int Id { get; set; }

    public int PostId { get; set; }

    public int MemberId { get; set; }

    public string CommentContent { get; set; } = null!;

    public DateTime Datetime { get; set; }

    public virtual Member Member { get; set; } = null!;

    public virtual Post Post { get; set; } = null!;
}
