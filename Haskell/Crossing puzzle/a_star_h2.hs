import Data.List (sort, delete)

data Side = AtBeginning | AtEnd deriving (Show, Eq) 

data State = State {
  leftSide :: [Int],
  rightSide :: [Int],
  sideOfLamp :: Side,
  backtracking :: Maybe State,
  trans :: Maybe String
} deriving (Show)

data Node = Node {
  state :: State,
  cost :: Int
} deriving (Show)

data NodeThunk = NodeThunk {
  left:: [Int],
  side :: Side,
  c :: Int
} deriving (Show)

goal :: State -> Bool
goal (State [] _ _ _ _)  = True
goal _                = False

mapping :: [String] -> [Int]
mapping ss = mapp ss []
  where mapp []     acc = acc
        mapp (x:xs) acc = mapp xs (insertOne (read x ::Int) acc)

initial :: [String] -> [Node]
initial [] = error "niste zadali niti jednog studenta, vas problem ne postoji !"
initial xs = [Node (State (mapping xs) [] AtBeginning Nothing Nothing) 0]

pairUp ::  Int -> [Int] -> [(Int,Int)]
pairUp _ [] = []
pairUp elem (x:xs) | elem <= x = (elem,x) : pairUp elem xs
                   | otherwise = (x,elem) : pairUp elem xs

pairs :: [Int] -> [(Int,Int)]
pairs []     = []
pairs (x:xs) = pairUp x xs ++ pairs xs

-- O(n), ordered					  
nubb :: Ord a => [a] -> [a]
nubb []     = []
nubb (x:xs) = x : loop x xs
  where
    loop _ [] = []
    loop x (y:ys)
       | (<) x y     = y : loop y ys
       | otherwise = loop x ys
				  
-- O(n), ordered list, e1 < e2
removePair :: Int -> Int -> [Int] -> [Int]
removePair _  _  []     = []
removePair e1 e2 (x:xs) | (e1  == x) = delete e2 xs 
                        | otherwise = x : (removePair e1 e2 xs)

-- O(n), ordered list
insertOne :: Int -> [Int] -> [Int]
insertOne e2 []     = [e2]
insertOne e2 (x:xs) = case compare e2 x of
                            GT -> x : insertOne e2 xs
                            _  -> e2 : x : xs

-- O(n), ordered list, e1 < e2
insertPair :: Int -> Int -> [Int] -> [Int]
insertPair e1 e2 []     = [e1,e2]
insertPair e1 e2 (x:xs) = case compare e1 x of
                            GT -> x : insertPair e1 e2 xs
                            _  -> e1 : insertOne e2 (x : xs)

minimum' []                 e _ acc = (e,acc)
minimum' (l@(Node s c1):ls) e f acc | ((c1 + newH) < f) == True = minimum' ls l (newH + c1) (e:acc)
                                    | otherwise = minimum' ls e f (l:acc)
  where newH = calculateHeuristic s
							
-- O (n), dovede minimalni clan na pocetak liste
minimize :: [Node] -> (Node, [Node])
minimize (x:xs) = minimum' xs x ((calculateHeuristic $ state x) + (cost x)) []  
 
											
-- O(n)
wasHiere :: Node -> [NodeThunk] -> Bool
wasHiere _ []       = False
wasHiere n@(Node (State l1 _ s1 _ _) c1) ((NodeThunk l2 s2 c2):nts) | (s1 == s2) && (l1 == l2) && (c1 >= c2) = True
                                                                    | otherwise = wasHiere n nts
succOfLeft :: [Int] -> [Int] -> [(Int,Int)] -> Int -> [NodeThunk] -> Maybe State -> [Node]
succOfLeft []    _    _          _      _      _ = []
succOfLeft [x]   r    _          offset _      p = [Node (State [] (insertOne x r) AtEnd p (Just (show x ++ " ->"))) (x + offset)]
succOfLeft [x,y] r    _          offset _      p = [Node (State [] (insertPair x y r) AtEnd p (Just (show x ++ ", " ++ show y ++ " ->"))) (y + offset)]
succOfLeft _     _    []         _      _      _ = []
succOfLeft l     r    ((x,y):xs) offset closed p | wasHiere newNode closed == True = succOfLeft l r xs offset closed p
                                                 | otherwise = newNode : succOfLeft l r xs offset closed p
  where newState = State (removePair x y l) (insertPair x y r) AtEnd p (Just (show x ++ ", " ++ show y ++ " ->"))
        newNode  = Node newState (offset + y)

succOfRight :: [Int] -> [Int] -> [Int] -> Int -> [NodeThunk] -> Maybe State -> [Node]
succOfRight _ [] _      _      _      _ =  []
succOfRight _ _  []     _      _      _ =  []
succOfRight l r  (x:xs) offset closed p | wasHiere newNode closed == True = succOfRight l r xs offset closed p
                                        | otherwise = newNode : succOfRight l r xs offset closed p
  where newState = State (insertOne x l) (delete x r) AtBeginning p (Just (show x ++ " <-"))
        newNode  = Node newState (offset + x)

uniformCostSearch :: [Node] -> (Node, Int)
uniformCostSearch [] = error "Nemoguce je konstruirati rjesenje, neobicno :P"
uniformCostSearch ns = unif ns [] 0 

unif :: [Node] -> [NodeThunk] -> Int -> (Node, Int) -- informacije kako doc do cvora i kolko je napravia iteracija
unif open closed acc | (goal $ state chosen) == True = (chosen, acc)
                     | otherwise = unif (newOpen ++ tp) newClosed (acc + 1) 
  where (chosen,tp) = minimize open	
        (newOpen, newClosed) = expand chosen closed

expand :: Node -> [NodeThunk] -> ([Node], [NodeThunk])
expand (Node s@(State l r side _ _) c) closed = (arg, getnewClosed closed arg) -- nova funkcija i nek ti vraca Maybe pair node, Set closed
  where arg | side == AtBeginning = succOfLeft l r (nubb $ pairs l) c closed (Just s)
            | otherwise = succOfRight l r (nubb r) c closed (Just s)

getnewClosed :: [NodeThunk] -> [Node] -> [NodeThunk]
getnewClosed closed []                            = closed
getnewClosed closed ((Node (State l _ s _ _) c):xs) = getnewClosed ((NodeThunk l s c):closed) xs

t1 = Node (State (sort [1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1]) [] AtBeginning Nothing Nothing) 0
t2 = Node (State (sort [1, 2, 5, 10, 12, 17, 24, 21, 20, 20, 11]) [] AtBeginning Nothing Nothing) 0

outputCost :: Int -> IO ()
outputCost cost = putStr $ show cost ++ ", "

outputVisited :: Int -> IO ()
outputVisited visited= putStrLn $ show visited

outputSolution :: State -> IO ()
outputSolution solution = putStr $ unlines $ prepareSolution solution

prepareSolution :: State -> [String]
prepareSolution state = preparation state []
  where preparation (State _ _ _ Nothing _) acc  = acc
        preparation s@(State _ _ _ (Just b) (Just t)) acc = preparation b (t : acc)

output :: String -> IO ()
output num = do
  outputCost cost
  outputVisited iterations
  outputSolution solution
    where ((Node solution cost), iterations) = uniformCostSearch $ initial $ words num

main :: IO ()
main = do
  numbers <- getLine
  output numbers

calculateHeuristic' :: [Int] -> Int
calculateHeuristic' []     = 0
calculateHeuristic' [x]    = x
calculateHeuristic' (x:y:xs) = x + calculateHeuristic' xs
  
calculateHeuristic :: State -> Int
calculateHeuristic (State [] _ _ _ _) = 0
calculateHeuristic (State l  _ _ _ _) = calculateHeuristic' $ reverse l

test = State [1, 2, 5, 10, 3, 4, 14, 18, 20 , 50] [] AtEnd Nothing Nothing