import csv
import pandas as pd

lheader = []
lcontent = []

with open('airtravel.csv') as csvf:
    print("hi")
    read = csv.reader(csvf, delimiter=',')
    lcount = 0
    print(type(read))
    for row in read:
        if lcount == 0:
            line = []
            for item in row:
                line.append(item.strip())
            lheader = line
            lcount += 1
        else:
            line = []
            line.append(row[0].strip())
            for item in row[1:]:
                line.append(int(item))
            lcontent.append(line)
            lcount += 1
    print(f'Process {lcount} lines.')


ldict = []
for line in lcontent:
    ldict.append(dict(zip(lheader,line)))

df_from_dict = pd.DataFrame(ldict)
    
df_from_list = pd.DataFrame(lcontent, columns = lheader)

df_from_csv = pd.read_csv('airtravel.csv')

df_for_test = pd.DataFrame(ldict)

additional_row_to_dict = {'Month': 'LOL', '"1958"': 870, '"1959"': 650, '"1960"': 420}

df_for_test.append(additional_row_to_dict, ignore_index = True)

additional_col_to_dict = [9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 1, 2]

df_for_test['Luna'] = additional_col_to_dict

df_for_test.Luna[2] = 30000
