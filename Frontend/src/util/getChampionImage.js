
import { championsAssets, championSplashAssets } from './importAssets';

// Helper function to get champion image by name
const getChampionImage = (name, type) => {
    // Convert to camelCase by removing spaces and single quotes, capitalizing each word except the first
    const formattedName = name
        .toLowerCase()
        .replace(/(?:\s|'|^)([a-z])/g, (match, p1, offset) => 
        offset === 0 ? p1 : p1.toUpperCase() // Capitalize each word except the first
        );
    
    if (type == "icon"){
        return championsAssets[formattedName] || championsAssets.noChampion;
    }
    
    // splash art
    return championSplashAssets[formattedName];
};
export default getChampionImage