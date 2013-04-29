package scholl.both.analyzer.social;

<<<<<<< HEAD
class SimpleUser {
    private final String name;
=======
class SimpleUser implements User {
    String name;
>>>>>>> 78e224b34b5c38d3e587740be6b906a2664e0da8
    
    public SimpleUser(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public String toString() {
        return name;
    }
<<<<<<< HEAD
    
    @Override
=======

>>>>>>> 78e224b34b5c38d3e587740be6b906a2664e0da8
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 78e224b34b5c38d3e587740be6b906a2664e0da8
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof SimpleUser))
            return false;
        SimpleUser other = (SimpleUser) obj;
        if (this.name == null) {
            if (other.name != null)
                return false;
        } else if (!this.name.equals(other.name))
            return false;
        return true;
    }
}