import Char (isUpper)
import Data.List (nub, (\\), intersect, delete, sort)

data Node = Atom Char     | 
            Ne Node       | 
            I Node Node   | 
            Ili Node Node | 
            Ako Node Node | 
            Akko Node Node deriving Show

instance Eq Node where
  (Atom a1)    == (Atom a2)    = (a1 == a2)
  (Ne a1)      == (Ne a2)      = (a1 == a2)
  (I l1 r1)    == (I l2 r2)    = ((l1 == l2) && (r1 == r2)) || ((l1 == r2) && (r1 == l2))
  (Ili l1 r1)  == (Ili l2 r2)  = ((l1 == l2) && (r1 == r2)) || ((l1 == r2) && (r1 == l2))
  (Ako l1 r1)  == (Ako l2 r2)  = ((l1 == l2) && (r1 == r2)) || ((l1 == r2) && (r1 == l2))
  (Akko l1 r1) == (Akko l2 r2) = ((l1 == l2) && (r1 == r2)) || ((l1 == r2) && (r1 == l2))
  _            == _            = False

data Literal = Yes Char | Not Char deriving Show

instance Eq Literal where
  (Yes c1) == (Yes c2) = (c1 == c2)
  (Not c1) == (Not c2) = (c1 == c2)
  _        == _        = False

getLetter :: Literal -> Char
getLetter (Yes c) = c
getLetter (Not c) = c

usporedi :: Literal -> Literal -> Ordering
usporedi (Yes _) (Not _) = GT
usporedi _       _       = LT

instance Ord Literal where
  l1 `compare` l2 | (getLetter l1) `compare` (getLetter l2) == EQ = usporedi l1 l2
                  | otherwise = (getLetter l1) `compare` (getLetter l2)

data Clause = Clause {
	poredak :: Int,
	literali :: [Literal],
	origin :: String
} deriving Show

instance Eq Clause where
  (Clause _ l1 _)  == (Clause _ l2 _)  = (l1 == l2)

instance Ord Clause where
  (Clause _ l1 _)  `compare` (Clause _ l2 _)  = (l1 `compare` l2)

isAtom :: Char -> Bool
isAtom = isUpper

isUnary :: Char -> Bool
isUnary x = x == '~'

isBinary :: Char -> Bool
isBinary x | x == '&' = True
           | x == '|' = True
           | x == '%' = True -- sinonim za -> Ako
           | x == '#' = True -- sinonim za <-> Akko
           | otherwise = False

transmute :: Char -> Node -> Node -> Node
transmute x | x == '&' = I
            | x == '|' = Ili
            | x == '%' = Ako  
            | x == '#' = Akko 

stringExplode :: [Char] -> ([Char], Node-> Node-> Node, [Char])
stringExplode ss = slr ss 0 []
  where slr []     _   _    = error "Greska u izrazu" -- ovaj se pokazuje redundatnim
        slr [x]    _   _    = error "Greska u izrazu"
        slr (s:xs) acc left | s == '(' = slr xs (acc-1) (left ++ [s])
                            | (acc == 0) && (isBinary(s) == True) = (left, transmute s, xs) 
                            | s == ')' = slr xs (acc+1) (left ++ [s])
                            | otherwise = slr xs acc (left ++ [s])

removeBrackets :: String -> String
removeBrackets [] = []
removeBrackets [x] = [x]
removeBrackets [x,y] = [x,y]
removeBrackets (x:xs) = init xs

build :: String -> Node
build [x] | isUpper(x) == True = Atom x
          | otherwise = error "Greska u formuli"
build [y,x] | (isUpper(x) == True) && (y == '~')= Ne (Atom x)
            | otherwise = error "Greska u formuli"
build ('~': '(': ss) = Ne (build $ init ss)
build ss = s (build $ removeBrackets l) (build $ removeBrackets r)
  where (l, s, r) = stringExplode ss

isItEquivalence :: String -> String
isItEquivalence (x:y:ss) | (x == '-') && (y == '>') = '#' : prepareStatement ss
                         |  otherwise = error "koristite nedopustene simbole !!"
isItEquivalence _        = error "koristite nedopustene simbole !!"

isItImlication :: String -> String
isItImlication []     = error "koristite nedopustene simbole !!"
isItImlication (c:ss) | c == '>' = '%' : prepareStatement ss
                      | otherwise = error "koristite nedopustene simbole !!"

prepareStatement :: String -> String
prepareStatement []     = []
prepareStatement (c:ss) | c == '~' = c : prepareStatement ss
                        | c == '&' = c : prepareStatement ss
                        | c == '|' = c : prepareStatement ss
                        | c == '-' = isItImlication ss
                        | c == '<' = isItEquivalence ss
                        | c == '(' = c : prepareStatement ss
                        | c == ')' = c : prepareStatement ss
                        | c == ' ' = prepareStatement ss
                        | isAtom(c) == True = c : prepareStatement ss
                        | otherwise = error "koristite nedopustene simbole !!"

buildExpressionTree :: String -> Node
buildExpressionTree [] = error "zadali ste .. PRAZNINU !!!"
buildExpressionTree ss = build $ prepareStatement ss
--
stdNeg :: Node -> Node
stdNeg (Ne n) = n -- involucija
stdNeg n = Ne n

stdAnd :: Node -> Node -> Node
stdAnd f g | f == g = f -- idempotencija
           | otherwise = I f g

stdOr :: Node -> Node -> Node
stdOr f g | f == g = f
          | otherwise = Ili f g -- faktorizacija

ruleNumberOne :: Node -> Node -> Node
ruleNumberOne f g = cnfStepOne $ stdAnd (stdOr (stdNeg f) g) (stdOr (stdNeg g) f)

ruleNumberTwo :: Node -> Node -> Node
ruleNumberTwo f g = cnfStepTwo $ stdOr (stdNeg f) g

deMorganA :: Node -> Node -> Node
deMorganA f g = cnfStepThree $ stdAnd (stdNeg f) (stdNeg g) 

deMorganB :: Node -> Node -> Node
deMorganB f g = cnfStepThree $ stdOr (stdNeg f) (stdNeg g) 

distributivnostA :: Node -> Node -> Node -> Node
distributivnostA f g h = cnfStepFour $ stdAnd (stdOr f g) (stdOr f h)

distributivnostB :: Node -> Node -> Node -> Node
distributivnostB f g h = cnfStepFour $ stdAnd (stdOr g f) (stdOr h f) 

cnfStepOne :: Node -> Node
cnfStepOne (Akko f g) = ruleNumberOne f g                           -- ruleNumberOne
cnfStepOne (Ne f)     = stdNeg (cnfStepOne f)
cnfStepOne (Ili f g)  = stdOr (cnfStepOne f) (cnfStepOne g)
cnfStepOne (I f g)    = stdAnd (cnfStepOne f) (cnfStepOne g)
cnfStepOne (Ako f g)  = Ako (cnfStepOne f) (cnfStepOne g)
cnfStepOne atom       = atom

cnfStepTwo :: Node -> Node
cnfStepTwo (Ako f g)  = ruleNumberTwo f g                           -- ruleNumberTwo
cnfStepTwo (Ne f)     = stdNeg (cnfStepTwo f)
cnfStepTwo (Ili f g)  = stdOr (cnfStepTwo f) (cnfStepTwo g)
cnfStepTwo (I f g)    = stdAnd (cnfStepTwo f) (cnfStepTwo g)
cnfStepTwo atom       = atom

cnfStepThree :: Node -> Node
cnfStepThree (Ne (Ili f g)) = deMorganA f g                         -- deMorgan a
cnfStepThree (Ne (I f g))   = deMorganB f g                           -- deMorgan b
cnfStepThree (Ne f)         = stdNeg (cnfStepThree f)
cnfStepThree (Ili f g)      = stdOr (cnfStepThree f) (cnfStepThree g)
cnfStepThree (I f g)        = stdAnd (cnfStepThree f) (cnfStepThree g)
cnfStepThree atom           = atom

cnfStepFour :: Node -> Node
cnfStepFour (Ili f (I g h)) = distributivnostA f g h               -- distributivnostA
cnfStepFour (Ili (I g h) f) = distributivnostB f g h               -- distributivnostB
cnfStepFour (Ili f g)       = stdOr (cnfStepFour f) (cnfStepFour g)
cnfStepFour (I f g)         = stdAnd (cnfStepFour f) (cnfStepFour g)
cnfStepFour atom            = atom

cnfConvert :: Node -> Node -- dvaput cnfFOUR
cnfConvert = cnfStepFour . cnfStepFour . cnfStepThree . cnfStepTwo . cnfStepOne
-- extra
reconstruct :: Node -> String
reconstruct (Atom a)  = [a]
reconstruct (Ne n)    = '(' : '~' : reconstruct n ++ ")"
reconstruct (I f g)   = '(' : reconstruct f ++ " & " ++ reconstruct g ++ ")"
reconstruct (Ili f g) = '(' : reconstruct f ++ " | " ++ reconstruct g ++ ")"

cnfConverter :: IO ()
cnfConverter = do
  line <- getLine
  putStrLn $ reconstruct $ cnfConvert (buildExpressionTree line)
-- END
-- Ordered list
joinNub :: [Literal] -> [Literal] -> [Literal] -- faktorizacija
joinNub []        []        = []
joinNub l1        []        = l1
joinNub []        l2        = l2
joinNub l1@(x:xs) l2@(y:ys) | x == y =  joinNub xs l2
                            | x < y = x : joinNub xs l2
                            | otherwise = y : joinNub l1 ys 
-- Ordered [Literal], exemplar [Not 'A', Yes 'A', Not 'B', Yes 'B']
createLiteralList :: Node -> [Literal]
createLiteralList (Atom a)      = [Yes a]
createLiteralList (Ne (Atom a)) = [Not a]
createLiteralList (Ili c1 c2)   = joinNub (createLiteralList c1) (createLiteralList c2)
createLiteralList o             = error $ show o

createLiterals :: Node -> [[Literal]]
createLiterals (I f g) = createLiterals f ++ createLiterals g
createLiterals node    = [createLiteralList node]

createSignature :: (Int, Int) -> String
createSignature (n1,n2) = '(' : show n1 ++ "," ++ show n2 ++ ")"

createClauses :: [[Literal]] -> Int -> String -> [Clause]
createClauses []     _     _         = []
createClauses (x:xs) mark  signature = (Clause mark x signature) : createClauses xs (mark+1) signature

collectClauses :: [[Literal]] -> Int -> String -> [Clause] -- poziva nub
collectClauses [] _    _         = []
collectClauses xs from signature = createClauses (nub xs) (from + 1) signature

negLiteral :: Literal -> Literal
negLiteral (Yes a) = Not a
negLiteral (Not a) = Yes a

existsLiteral :: Literal -> [Literal] -> Bool
existsLiteral _ []     = False
existsLiteral x (y:ys) | x == y = True
                       | getLetter x >= getLetter y = existsLiteral x ys
                       | otherwise = False
				
pairUp :: Int -> Int -> (Int, Int)
pairUp x y | x < y = (x,y)
           | otherwise = (y,x)

initialNil :: [[Literal]] -> Bool
initialNil all = inspect all all
  where inspect []       _  = False
        inspect ([lit]:xs) ys | member [negLiteral lit] ys == True = True
                              | otherwise = inspect xs ys
        inspect (x:xs)     ys = inspect xs ys

getClause :: [Clause] -> Int -> [Literal]
getClause ((Clause n lit _):xs) num | num == n = lit
                                    | otherwise = getClause xs num
					   
nil :: [[Literal]] -> [[Literal]] -> Bool
nil []     _    = False
nil ([lit]:xs) memo | member [negLiteral lit] memo == True = True
                    | otherwise = nil xs memo
nil (x:xs)     memo = nil xs memo

literalToString :: Literal -> String
literalToString (Yes a) = [a]
literalToString (Not a) = '~' : [a]

clauseToString :: [Literal] -> String
clauseToString [x]    = literalToString x
clauseToString (x:xs) = literalToString x ++ "v" ++ clauseToString xs
clauseTOString c      = error ("\n\n" ++ (show c))

stepToString :: Clause -> String
stepToString (Clause num lit sig) = show num ++ ".\t" ++ clauseToString lit ++ "\t\t " ++ sig

collectSteps :: [Clause] -> [String]
collectSteps []     = []
collectSteps (x:xs) = stepToString x : collectSteps xs

thereIs :: Literal -> [Clause] -> Int
thereIs _ []                      = 0
thereIs x ((Clause num [y] _):ys) | x == y = num
                                  | otherwise = thereIs x ys
thereIs x (z:zs)                  = thereIs x zs
					  
whoWereThey :: [Clause] -> (Int, Int)
whoWereThey all = search all all
  where search ((Clause num [lit] _):xs) ys | thereIs (negLiteral lit) ys /= 0 = (num, thereIs (negLiteral lit) ys)
                                            | otherwise = search xs ys
        search (x:xs)                    ys = search xs ys

whoWasIt :: [[Literal]] -> [[Literal]] -> [Literal]
whoWasIt [x]        _    = x 
whoWasIt ([x]:xs) memo | member [negLiteral x] memo == True = [x]
                       | otherwise = whoWasIt xs memo
whoWasIt (y:ys)   memo =  whoWasIt ys memo

chooseTheOneThatWas :: [Literal] -> [Clause] -> Clause
chooseTheOneThatWas l (c@(Clause _ lit _):xs) | l == lit = c
                                              | otherwise = chooseTheOneThatWas l xs

output :: Bool -> Int -> [Clause] -> [String]
output dokazivost brojKoraka skupovi = [show dokazivost] ++ ["broj koraka:\t" ++ show brojKoraka] ++ ["Najveci broj klauzula u memoriji:\t" ++ (show $ length skupovi)] ++ collectSteps skupovi
-- DO SADA SVE SU FUNKCIJE BILE ZAJEDNICKE DIJELU NULA I JEDAN !!
canTheyResolve0 :: [Literal] -> [Literal] -> Bool
canTheyResolve0 []     _    = False
canTheyResolve0 (x:xs) (ys) | existsLiteral (negLiteral x) ys == True = True
                            | otherwise = canTheyResolve0 xs ys

collectPairsWith0 :: Clause -> [Clause] -> [(Int,Int)]
collectPairsWith0 _                  []                      = []
collectPairsWith0 c@(Clause num1 lit1 _) ((Clause num2 lit2 _):ys) | (num1 /= num2) && (canTheyResolve0 lit1 lit2) == True = (pairUp num1 num2) : collectPairsWith0 c ys
                                                                   | otherwise = collectPairsWith0 c ys

collectPairs0 :: [Clause] -> [Clause] -> [(Int, Int)]			
collectPairs0 support all = nub $ roughPairs support all
  where roughPairs []          _   = []
        roughPairs (x:xs) ys = (collectPairsWith0 x ys) ++ (roughPairs xs ys)

makeCleanRezolvent :: (Int, Int) -> [Clause] -> ([Literal], [Literal])
makeCleanRezolvent (num1, num2) list = ((rez1 ++ rez2), (intersect rez1 (map negLiteral rez2)))
  where rez1 = getClause list num1
        rez2 = getClause list num2

makeRezolvent0 :: [Literal] -> Literal-> [Literal]
makeRezolvent0 [] _      = []
makeRezolvent0 xs y = (delete (negLiteral y) (delete y xs)) 

collectLiterals0 :: [String] -> [[Literal]]
collectLiterals0 []     = []
collectLiterals0 (s:ss) = (createLiterals $ cnfConvert $ buildExpressionTree s) ++ (collectLiterals0 ss)

createRezolvents0 :: [Literal] -> [Literal] -> [[Literal]] -> [[Literal]]
createRezolvents0 _     []     _    = []
createRezolvents0 rough (x:xs) memo | isTautology newMaterial == True = createRezolvents0 rough xs memo
                                    | newMaterial == [] = createRezolvents0 rough xs memo -- eto odlucio sam se izbacivat tautologije
                                    | member newMaterial memo == False = newMaterial : createRezolvents0 rough xs memo
                                    | otherwise = createRezolvents0 rough xs memo
  where newMaterial = sort $ nub $ makeRezolvent0 rough x

collectRezolvents0 :: (Int,Int) -> [Clause] -> [[Literal]] -> [[Literal]]
collectRezolvents0 (n1,n2) clausesList memo = createRezolvents0 cleanRezolvent operations memo
  where (cleanRezolvent, operations) = makeCleanRezolvent (n1,n2) clausesList

member :: [Literal] -> [[Literal]] -> Bool
member x []     = False
member x (y:ys) | x == y = True
                | otherwise = member x ys

plResolution0 :: [String] -> [String]
plResolution0 (s:ss) | (initialNil allLiterals) == True = (output True 0 (clausesList ++ suppList)) ++ ["NIL" ++ (show $ whoWereThey (clausesList ++ suppList))]
                     | otherwise = stratSupp clausesList suppList allLiterals listOfTransPairs 1
  where suppLiterals     = createLiterals $ cnfConvert $ stdNeg $ buildExpressionTree s
        clausesLiterals  = (collectLiterals0 ss) \\ suppLiterals
        allLiterals      = clausesLiterals ++ suppLiterals
        clausesList      = collectClauses clausesLiterals 0 "pocetne klauzule"
        suppList         = collectClauses suppLiterals (length clausesList) "pocetni skup potpore"
        listOfTransPairs = collectPairs0 suppList (suppList ++ clausesList)
plResolution0 _      = error "Niste unjeli bar jednu premisu"			

stratSupp :: [Clause] -> [Clause] -> [[Literal]] -> [(Int,Int)] -> Int -> [String]
stratSupp clauses supp _    []     acc = output False acc (clauses ++ supp)
stratSupp clauses supp memo (x:xs) acc | nil newRezolvents memo == True = (output True acc final) ++ [(show $ (length final+1)) ++  ".\tNIL\t"  ++ (show $ whoWereThey final)] 
                                       | otherwise = stratSupp clauses (supp ++ newClauses) (memo ++ newRezolvents) (newTransformations ++ xs ) (acc+1)
  where all                = clauses ++ supp
        newRezolvents      = collectRezolvents0 x all memo
        newClauses         = collectClauses newRezolvents (length all) (createSignature x)
        newTransformations = collectPairs0 newClauses all
        final              = clauses ++ supp ++ [chooseTheOneThatWas (whoWasIt newRezolvents memo) newClauses]
--
canTheyResolve1 :: [Literal] -> [Literal] -> Int -> Int
canTheyResolve1 []     _    acc = acc
canTheyResolve1 (x:xs) (ys) acc | existsLiteral (negLiteral x) ys == True = canTheyResolve1 xs ys (acc + 1)
                                | otherwise = canTheyResolve1 xs ys acc

collectPairsWith1 :: Clause -> [Clause] -> [(Int,Int)]
collectPairsWith1 _                  []                      = []
collectPairsWith1 c@(Clause num1 lit1 _) ((Clause num2 lit2 _):ys) | (num1 /= num2) && (canTheyResolve1 lit1 lit2 0) == 1 = (pairUp num1 num2) : collectPairsWith1 c ys
                                                                   | otherwise = collectPairsWith1 c ys

collectPairs1 :: [Clause] -> [Clause] -> [(Int, Int)]			
collectPairs1 support all = nub $ roughPairs support all
  where roughPairs []          _   = []
        roughPairs (x:xs) ys = (collectPairsWith1 x ys) ++ (roughPairs xs ys)

makeRoughRezolvent :: (Int, Int) -> [Clause] -> [Literal]
makeRoughRezolvent (num1, num2) list = joinNub (getClause list num1) (getClause list num2)

inspectRoughRezolvent :: [Literal] -> [Char]
inspectRoughRezolvent []       = []
inspectRoughRezolvent [x]      = []
inspectRoughRezolvent (x:y:xs) | getLetter x == getLetter y = getLetter x : inspectRoughRezolvent xs
                               | otherwise = inspectRoughRezolvent xs

makeRezolvent1 :: [Literal] -> Char -> [Literal]
makeRezolvent1 []     _ = []
makeRezolvent1 (x:xs) c | getLetter x == c = makeRezolvent1 xs c
                        | getLetter x < c  = x : makeRezolvent1 xs c
                        | otherwise = x:xs

isTautology :: [Literal] -> Bool
isTautology []  = False
isTautology [x] = False
isTautology (x:y:s) | getLetter x == getLetter y = True
                    | otherwise = isTautology (y:s)

isPartOf :: [Literal] -> [Literal] -> Bool
isPartOf []     []     = True
isPartOf []     _      = False
isPartOf _      []     = True
isPartOf (x:xs) (y:ys) | x == y = isPartOf xs ys
                       | otherwise = False

canBeAbsorbedBy :: [Literal] -> [Literal] -> Bool
canBeAbsorbedBy []     _      = False
canBeAbsorbedBy (x:xs) subset | headSubset == x = isPartOf xs (tail subset)
                              | getLetter x > getLetter headSubset = False 
                              | otherwise = canBeAbsorbedBy xs subset
  where headSubset = head subset

canBeAbsorbed :: [Literal] -> [[Literal]] -> Bool -- Provjerava i da li je podskup ili pravi podskup, moze zaminit member
canBeAbsorbed x []     = False
canBeAbsorbed x (y:ys) | canBeAbsorbedBy x y == True = True
                       | otherwise = canBeAbsorbed x ys
   
absorbAmongUs :: [[Literal]] -> [[Literal]]
absorbAmongUs []  = []
absorbAmongUs [x] = [x]
absorbAmongUs all = investigate all []
  where investigate []     acc = acc
        investigate (x:xs) acc | canBeAbsorbed x (xs ++ acc) == True = investigate xs acc
                               | otherwise = investigate xs (x : acc)

removeUnnecessary :: [[Literal]] -> [[Literal]]
removeUnnecessary []     = []
removeUnnecessary (x:xs) | isTautology x == True = removeUnnecessary xs
                         | otherwise = x : removeUnnecessary xs

removeRedundance :: [[Literal]] -> [[Literal]] -> [[Literal]]
removeRedundance []     memo = []
removeRedundance (x:xs) memo | canBeAbsorbed x memo == True = removeRedundance xs memo
                             | otherwise = x : removeRedundance xs memo

collectLiterals1 :: [String] -> [[Literal]] -- removes unnecesary but not rendundance
collectLiterals1 []     = []
collectLiterals1 (s:ss) = newLiterals ++ (collectLiterals1 ss)
  where newLiterals = removeUnnecessary $ createLiterals $ cnfConvert $ buildExpressionTree s
-- optimizirano ljudskomn logikom :D -- ispod neoptimizirana verzija, daju jednak rezultat glede koraka
createRezolvents1 :: [Literal] -> [Char] -> [[Literal]] -> [[Literal]]
createRezolvents1 _     []     _    = []
createRezolvents1 rough [x] memo | isTautology newMaterial == True = []
                                 | newMaterial == [] = []
                                 | canBeAbsorbed newMaterial memo == True = []
                                 | otherwise = [newMaterial]
  where newMaterial = makeRezolvent1 rough x
createRezolvents1 _     _     _     = []  
{-
createRezolvents1 :: [Literal] -> [Char] -> [[Literal]] -> [[Literal]]
createRezolvents1 _     []     _    = []
createRezolvents1 rough (x:xs) memo | isTautology newMaterial == True = createRezolvents1 rough xs memo
                                    | newMaterial == [] = createRezolvents1 rough xs memo
                                    | canBeAbsorbed newMaterial memo == True = createRezolvents1 rough xs memo
                                    | otherwise = newMaterial : createRezolvents1 rough xs memo
  where newMaterial = makeRezolvent1 rough x
-}
collectRezolvents1 :: (Int,Int) -> [Clause] -> [[Literal]] -> [[Literal]]
collectRezolvents1 (n1,n2) clausesList memo = createRezolvents1 roughRezolvent operations memo
  where roughRezolvent = makeRoughRezolvent (n1,n2) clausesList
        operations     = inspectRoughRezolvent roughRezolvent

plResolution1 :: [String] -> [String]
plResolution1 (s:ss) | (initialNil allLiterals) == True = (output True 0 (clausesList ++ suppList)) ++ ["NIL" ++ (show $ whoWereThey (clausesList ++ suppList))]
                     | otherwise = stratSuppStratSimp clausesList suppList allLiterals listOfTransPairs 1
  where suppLiterals     = absorbAmongUs $ removeUnnecessary $ createLiterals $ cnfConvert $ stdNeg $ buildExpressionTree s
        clausesLiterals  = absorbAmongUs $ (collectLiterals1 ss) \\ suppLiterals
        allLiterals      = clausesLiterals ++ suppLiterals
        clausesList      = collectClauses (removeRedundance clausesLiterals suppLiterals) 0 "pocetne klauzule" -- removes redundance
        suppList         = collectClauses (removeRedundance suppLiterals clausesLiterals) (length clausesList) "pocetni skup potpore" -- removes redundance
        listOfTransPairs = collectPairs1 suppList (suppList ++ clausesList)
plResolution1 _      = error "Niste unjeli bar jednu premisu"		

stratSuppStratSimp :: [Clause] -> [Clause] -> [[Literal]] -> [(Int,Int)] -> Int -> [String]
stratSuppStratSimp clauses supp _    []     acc = output False acc (clauses ++ supp)
stratSuppStratSimp clauses supp memo (x:xs) acc | nil newRezolvents memo == True = (output True acc final) ++ [(show $ (length final+1)) ++  ".\tNIL\t" ++ (show $ whoWereThey final)] 
                                                | otherwise = stratSuppStratSimp clauses (supp ++ newClauses) (memo ++ newRezolvents) (newTransformations ++ xs) (acc+1)
  where all                = clauses ++ supp
        newRezolvents      = collectRezolvents1 x all memo 
        newClauses         = collectClauses newRezolvents (length all) (createSignature x)
        newTransformations = collectPairs1 newClauses all
        final              = clauses ++ supp ++ [chooseTheOneThatWas (whoWasIt newRezolvents memo) newClauses]
-- IO 
isItOver :: String -> Bool
isItOver "0" = True
isItOver "1" = True
isItOver _   = False

main :: IO ()
main = input []

input :: [String] -> IO ()
input acc = do  
  line <- getLine  
  if isItOver line  
    then if line == "0"
      then putStr $ unlines $ plResolution0 acc
      else putStr $ unlines $ plResolution1 acc
    else do  
    input (line : acc)

	
	
	
	
	
	
	

charles1 = "F"
charles2 = "F|G"

vanna1 = "(A&B)<->C"
vanna2 = "C->B"

vana1 = "((~P & Q) <-> (R|S)) | (~P -> S)"
vana2 = "((~S & R) -> (Q & P)) | ((P & R) | Q)"

claus1 = Clause 0 [Yes 'D']
claus2 = Clause 2 [Yes 'A', Yes 'B', Yes 'D']
claus3 = Clause 3 [Yes 'D']
claus4 = Clause 4 [Yes 'A', Yes 'B', Not 'D']

exampleCanTheyResolve = canTheyResolve0 [Not 'A', Not 'B', Not 'C'] [Not 'A', Not 'B',Yes 'C']

oho = existsLiteral (Not 'C') [Not 'A', Not 'B',Not 'C']

test1 = cnfConvert $ buildExpressionTree "(C | D) -> (~A <-> B)"

example = "((((C | D) -> (~A <-> B)) <-> ((C | D) -> (~A <-> B)))<->(((C | D) -> (~A <-> B)) <-> ((C | D) -> (~A <-> B))))<->((((C | D) -> (~A <-> B)) <-> ((C | D) -> (~A <-> B)))<->(((C | D) -> (~A <-> B)) <-> ((C | D) -> (~A <-> B))))"

test = joinNub (joinNub (joinNub (t1) (t2)) (t1)) (joinNub (t2) (joinNub t1 t2))

t1 = [Not 'A']
t2 = [Yes 'A']
t3 = [Not 'B']
t4 = [Yes 'B']

node = Atom 'A'
node2 = Ne node
node3 = Ili node node2
node4 = Ili node3 node
node5 = Ili node4 (Atom 'B')