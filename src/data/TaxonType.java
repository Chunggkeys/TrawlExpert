package data;

/**
 * Enumeration of classifications of Taxa.
 * @author Christopher W. Schankula
 *
 */

//Changed to only capitalizing the first character
public enum TaxonType {
	Kingdom, Phylum, Class, Order,
	Family, Genus, Species, Subspecies;
	
	public String toString() {
		return name();
	}
}