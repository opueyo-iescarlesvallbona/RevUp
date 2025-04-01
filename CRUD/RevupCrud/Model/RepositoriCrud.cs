using RepositoriRevUp;
using RepositoriRevUp.Model;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace RevupCrud.Model
{
    public class RepositoriCrud
    {
        public RepositoriCrud()
        {
            Repositori.dbConnect();
        }

        void SaveChanges()
        {
            Repositori.db.SaveChanges();
        }

        public List<gender> GetAllGenders()
        {
            List<gender> genders = new List<gender>();
            try
            {
                genders = Repositori.db.genders.ToList();
            }
            catch
            {
                Repositori.dbConnect();
            }
            return genders;
        }

        public List<member_location> GetAllLocations()
        {
            List<member_location> locations = new List<member_location>();
            try
            {
                locations = Repositori.db.member_location.ToList();
            }
            catch
            {
                Repositori.dbConnect();
            }
            return locations;
        }

        public member_location GetLocationById(int id)
        {
            member_location location = new member_location();
            try
            {
                location = Repositori.db.member_location.Where(x=>x.id.Equals(id)).FirstOrDefault();
            }
            catch
            {
                Repositori.dbConnect();
            }
            return location;
        }

        public List<member> GetAllMembers(string name, string gender, string year, string location)
        {
            List<member> members = new List<member>();
            try
            {
                if (gender.Equals("Tots"))
                {
                    gender = "";
                }
                if(location.Equals("Tots"))
                {
                    location = "";
                }
                var query = Repositori.db.members.AsQueryable();

                if (!string.IsNullOrEmpty(name))
                {
                    query = query.Where(x => x.name.Contains(name) || x.membername.Contains(name));
                }

                if (!string.IsNullOrEmpty(gender))
                {
                    if (gender.Equals(""))
                    {
                        query = query.Where(x => x.gender.name.Contains(gender));
                    }
                    else
                    {
                        query = query.Where(x => x.gender.name.Equals(gender));
                    }
                    
                }

                if (!string.IsNullOrEmpty(year))
                {
                    query = query.Where(x => x.date_of_birth.Year.ToString().Equals(year));
                }

                if (!string.IsNullOrEmpty(location))
                {
                    query = query.Where(x => x.member_location.municipality.Contains(location));
                }

                members = query.ToList();
            }
            catch
            {
                Repositori.dbConnect();
            }
            return members;
        }

        public List<club> GetAllClubs()
        {
            List<club> clubs = new List<club>();
            try
            {
                clubs = Repositori.db.clubs.ToList();
            }
            catch
            {
                Repositori.dbConnect();
            }
            return clubs;
        }

        public List<post> GetAllPosts()
        {
            List<post> posts = new List<post>();
            try
            {
                posts = Repositori.db.posts.ToList();
            }
            catch
            {
                Repositori.dbConnect();
            }
            return posts;
        }

        public List<member> GetFriends(int id)
        {
            List<member> friends = new List<member>();
            try
            {
                friends = Repositori.db.member_relation.Where(x => x.member_id1.Equals(id)).Select(x => x.member1).ToList();
            }
            catch
            {
                Repositori.dbConnect();
            }
            return friends;
        }

        public List<club> GetClubs(int id)
        {
            List<club> clubs = new List<club>();
            try
            {
                clubs = Repositori.db.member_club.Where(x => x.member_id.Equals(id)).Select(x => x.club).ToList();
            }
            catch
            {
                Repositori.dbConnect();
            }
            return clubs;
        }

        public bool AddMember(member usuari)
        {
            bool afegit = false;
            try
            {
                Repositori.db.members.Add(usuari);
                SaveChanges();
                MessageBox.Show("Usuari afegit correctament");
                afegit = true;
            }
            catch
            {
                MessageBox.Show("Error al afegir l'usuari");
                Repositori.dbConnect();
            }
            return afegit;
        }

        public void UpdateMember(member usuari)
        {
            try
            {
                member m = Repositori.db.members.Where(x => x.id.Equals(usuari.id)).FirstOrDefault();

                m.name = usuari.name;
                m.membername = usuari.membername;
                m.experience = usuari.experience;
                m.gender_id = usuari.gender_id;
                m.location_id = usuari.location_id;
                m.date_of_birth = usuari.date_of_birth;
                SaveChanges();
                MessageBox.Show("Usuari actualitzat correctament");
            }
            catch
            {
                MessageBox.Show("Error al actualitzar l'usuari");
                Repositori.dbConnect();
            }
        }

        public bool DeleteMember(member usuari)
        {
            bool esborrat = false;
            try
            {
                member m = Repositori.db.members.Where(x => x.id.Equals(usuari.id)).FirstOrDefault();
                Repositori.db.members.Remove(m);
                SaveChanges();
                MessageBox.Show("Usuari esborrat correctament");
                esborrat = true;
            }
            catch
            {
                MessageBox.Show("Error al eliminar l'usuari");
                Repositori.dbConnect();
            }
            return esborrat;
        }
    }
}
