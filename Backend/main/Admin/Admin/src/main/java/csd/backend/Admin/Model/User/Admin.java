package csd.backend.Admin.Model.User;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("admin")  // This defines the discriminator value for this subclass
public class Admin extends User {
}
