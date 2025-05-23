//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace RepositoriRevUp.Model
{
    using System;
    using System.Collections.Generic;
    
    public partial class route
    {
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public route()
        {
            this.posts = new HashSet<post>();
        }
    
        public int id { get; set; }
        public string name { get; set; }
        public string waypoints { get; set; }
        public decimal distance { get; set; }
        public long duration { get; set; }
        public Nullable<decimal> max_elevation { get; set; }
        public Nullable<decimal> elevation_gain { get; set; }
        public string start_address { get; set; }
        public string end_address { get; set; }
        public Nullable<int> terrain_type_id { get; set; }
        public string description { get; set; }
        public int member_id { get; set; }
        public Nullable<System.DateTime> datetime { get; set; }
    
        public virtual member member { get; set; }
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<post> posts { get; set; }
        public virtual terrain_type terrain_type { get; set; }
    }
}
