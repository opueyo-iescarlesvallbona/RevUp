﻿using RepositoriRevUp;
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

        public List<member> GetMembersById(List<int> ids)
        {
            List<member> members = new List<member>();
            try
            {
                foreach(int id in ids)
                {
                    members.Add(Repositori.db.members.Where(x => x.id.Equals(id)).FirstOrDefault());
                }
            }
            catch
            {
                Repositori.dbConnect();
            }
            return members;
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
                friends = Repositori.db.member_relation.Where(x => x.member_id1.Equals(id) || x.member_id2.Equals(id)).Select(x => x.member1).ToList();
            }
            catch
            {
                Repositori.dbConnect();
            }
            return friends;
        }

        public List<member_club> GetMemberClubsByMemberId(int id)
        {
            List<member_club> clubs = new List<member_club>();
            try
            {
                clubs = Repositori.db.member_club.Where(x => x.member_id.Equals(id)).ToList();
            }
            catch
            {
                Repositori.dbConnect();
            }
            return clubs;
        }

        public List<member_club> GetMemberClubsByMemberIdIsFounder(int id)
        {
            List<member_club> clubs = new List<member_club>();
            try
            {
                clubs = Repositori.db.member_club.Where(x => x.member_id.Equals(id) && x.club.founder.Equals(id)).ToList();
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
                m.description = usuari.description;
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
                MessageBox.Show("Error al esborrar l'usuari");
                Repositori.dbConnect();
            }
            return esborrat;
        }

        public List<club_event> GetEventsByClub(int id)
        {
            List<club_event> events = new List<club_event>();
            try
            {
                events = Repositori.db.club_event.Where(x => x.club_id.Equals(id)).ToList();
            }
            catch
            {
                Repositori.dbConnect();
            }
            return events;
        }

        public List<member_club> GetMemberClubsByClubId(int id)
        {
            List<member_club> member_clubs = new List<member_club>();
            try
            {
                member_clubs = Repositori.db.member_club.Where(x => x.club_id.Equals(id)).ToList();
            }
            catch
            {
                Repositori.dbConnect();
            }
            return member_clubs;
        }

        public bool DeleteClub(club club)
        {
            bool esborrat = false;
            try
            {
                club c = Repositori.db.clubs.Where(x => x.id.Equals(club.id)).FirstOrDefault();
                Repositori.db.clubs.Remove(c);
                SaveChanges();
                MessageBox.Show("Club esborrat correctament");
                esborrat = true;
            }
            catch
            {
                MessageBox.Show("Error al esborrar el club");
                Repositori.dbConnect();
            }
            return esborrat;
        }

        public bool AddClub(club club)
        {
            bool afegit = false;
            try
            {
                Repositori.db.clubs.Add(club);
                SaveChanges();
                MessageBox.Show("Club afegit correctament");
                afegit = true;
            }
            catch
            {
                MessageBox.Show("Error al afegir el club");
                Repositori.dbConnect();
            }
            return afegit;
        }

        public void UpdateClub(club club)
        {
            try
            {
                club c = Repositori.db.clubs.Where(x => x.id.Equals(club.id)).FirstOrDefault();

                c.name = club.name;
                c.founder = club.founder;
                c.description = club.description;
                SaveChanges();
                MessageBox.Show("Club actualitzat correctament");
            }
            catch
            {
                MessageBox.Show("Error al actualitzar el club");
                Repositori.dbConnect();
            }
        }

        public List<post_type> GetAllPostTypes()
        {
            List<post_type> post_types = new List<post_type>();
            try
            {
                post_types = Repositori.db.post_type.ToList();
            }
            catch
            {
                Repositori.dbConnect();
            }
            return post_types;
        }

        public List<car> GetMemberCars(int id)
        {
            List<car> cars = new List<car>();
            try
            {
                cars = Repositori.db.cars.Where(x => x.member_id.Equals(id)).ToList();
            }
            catch
            {
                Repositori.dbConnect();
            }
            return cars;
        }
    }
}
