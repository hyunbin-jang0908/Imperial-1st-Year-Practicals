module MP (separators, lookUp, splitText, combine, getKeywordDefs, expand) where
import Text.ParserCombinators.ReadP (get)
import GHC.IO.Encoding (TextEncoding(textEncodingName))
import Data.Bits (Bits(xor))

type FileContents = String

type Keyword      = String
type KeywordValue = String
type KeywordDefs  = [(Keyword, KeywordValue)]

separators :: String
separators = " \n\t.,:;!\"\'()<>/\\"

-----------------------------------------------------

{-|
This function will look up a key in a list of key-value pairs,
returning all the values that match with that key.

> lookUp "A" [("A", 8), ("B", 9), ("C", 5), ("A", 7)] == [8, 7]
-}
lookUp :: String -> [(String, a)] -> [a]
lookUp a xs = [snd x | x <- xs, fst x == a]

{-|
This function will break up a string with some given separator
characters, returning both the list of separators found between
each "word" and the words themselves.
-}
splitText :: [Char] -- ^ the separators to split on
          -> String -- ^ the string to split
          -> ([Char], [String])
splitText _ [] = ([], [""])         -- base case
splitText xs (y:ys)
       | y `elem` xs = insertSep y (splitText xs ys)
       | otherwise   = insertWord y (splitText xs ys)
       where
       insertSep :: Char -> ([Char], [String]) -> ([Char], [String])
       insertSep a (xs, y) = (a : xs, "" : y)
       insertWord :: Char -> ([Char], [String]) -> ([Char], [String])
       insertWord a (xs, y:ys) = (xs, (a : y) : ys)

{-|
This function interleaves the characters from the first argument
list with the strings in the second argument. The second list must
be non-empty.
-}
combine :: [Char] -> [String] -> [String]
combine seps []       = []
combine [] xs         = xs
combine (y:ys) (x:xs) = x : [y] : combine ys xs  -- y::Char, ys::[Char], x::String, xs::[String]

{-|
This function takes a list of lines and splits each line to
extract a list of keyword-definition pairs.

> getKeywordDefs ["$x Define x", "$y 55"] == [("$x", "Define x"), ("$y", "55")]
-}
-- type KeywordDefs = [(String, String)]
getKeywordDefs :: [String] -> KeywordDefs
getKeywordDefs [] = []
getKeywordDefs (x:xs) = (keyword, concat (combine (tail seps) keywordValue)) : getKeywordDefs xs
       where (seps, keyword:keywordValue) = splitText " " x

{-|
This function takes the contents of two files, one containing
a template and the other the definitions for each keyword
found within the template. It extracts the keyword-definition
information from the info file and uses it to expand the occurrences
of these keywords in the template file, producing new file contents
as a result.

> expand "The capital $1 is $2" "$1 Peru\n$2 Lima." == "The capital of Peru is Lima"
-}

-- "$name William Shakespeare\n$birth-date 1564\n$town Stratford upon Avon\n#\n$name Thomas hardy\n$birth-date 1840\n$town Stinsford"
-- ["$name William Shakespeare","$birth-date 1564","$town Stratford upon Avon","#","$name Thomas hardy"]
-- defs = ("$name","William Shakespeare"),("$birth-date","1564"),("$town","Stratford upon Avon"),("#",""),("$name","Thomas hardy")]

expandSub :: FileContents -- ^ the template file contents
       -> FileContents -- ^ the info file contents
       -> FileContents
expandSub "" infos        = ""
expandSub contents infos
       | x == ""       = head seps : expandSub restContents infos
       | null seps     = replaceWord x defs ++ expandSub restContents infos  -- when you are processing the last word
       | seps /= []    = replaceWord x defs ++ [head seps] ++ expandSub restContents infos
       where
       defs         = getKeywordDefs (snd (splitText "\n" infos))
       restContents = concat (combine (tail seps) xs)
       (seps, x:xs) = splitText separators contents

-- You may wish to uncomment and implement this helper function
-- when implementing expand
-- type KeywordDefs  = [(Keyword, KeywordValue)]
-- e.g. replaceWord "$1" [("$1", "Peru"), ("$2", "55")]  == "Peru"
-- if nothing matches with the input String, returns the string itself.
       replaceWord :: String -> KeywordDefs -> String
       replaceWord keyword defs
              | head keyword == '$' = concat (lookUp keyword defs)
              | otherwise           = keyword


expand :: FileContents -- ^ the template file contents
       -> FileContents -- ^ the info file contents
       -> FileContents
expand contents []        = ""
expand contents infos = expandSub contents z ++ "-----\n" ++ expand contents restInfo
--      | null zs = expandSub contents z ++ "-----\n"
--      | otherwise = expandSub contents z ++ "-----\n" ++ expand contents restInfo
       where
       (seps, z:zs) = splitText "#" infos
       restInfo = concat (combine (tail seps) zs)

